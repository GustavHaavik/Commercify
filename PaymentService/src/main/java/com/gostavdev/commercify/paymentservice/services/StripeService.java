package com.gostavdev.commercify.paymentservice.services;

import com.gostavdev.commercify.paymentservice.OrderClient;
import com.gostavdev.commercify.paymentservice.dto.OrderDTO;
import com.gostavdev.commercify.paymentservice.dto.OrderLineDTO;
import com.gostavdev.commercify.paymentservice.dto.PaymentRequest;
import com.gostavdev.commercify.paymentservice.dto.PaymentResponse;
import com.gostavdev.commercify.paymentservice.entities.PaymentEntity;
import com.gostavdev.commercify.paymentservice.entities.PaymentStatus;
import com.gostavdev.commercify.paymentservice.exceptions.InvalidEventDataException;
import com.gostavdev.commercify.paymentservice.repositories.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Product;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StripeService {
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    public PaymentResponse checkoutSession(PaymentRequest paymentRequest) {
        OrderDTO order = orderClient.getOrderById(paymentRequest.orderId());
        if (order == null) {
            throw new InvalidEventDataException("Order not found");
        }

        List<OrderLineDTO> orderLineDTOS = order.orderLines();

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(paymentRequest.successUrl())
                        .setCancelUrl(paymentRequest.cancelUrl())
                        .setCurrency(paymentRequest.currency())
                        .addAllLineItem(orderLineDTOS.stream().map(ol -> {
                            Long quantity = Long.valueOf(ol.quantity());
                            if (quantity <= 0)
                                throw new InvalidEventDataException("Invalid quantity for order line");

                            SessionCreateParams.LineItem.Builder lineItem = SessionCreateParams.LineItem.builder()
                                    .setQuantity(quantity);

                            try {
                                Product product = Product.retrieve(ol.stripeProductId());
                                lineItem.setPrice(product.getDefaultPrice());
                            } catch (StripeException e) {
                                throw new RuntimeException(e);
                            }

                            return lineItem.build();
                        }).toList())
                        .putMetadata("orderId", paymentRequest.orderId().toString())
                        .build();
        try {
            // Create the PaymentIntent
            Session session = Session.create(params);

            // Save the payment in our local database with status "PENDING"
            PaymentEntity payment = new PaymentEntity(paymentRequest);
            paymentRepository.save(payment);

            System.out.println("Payment session created: " + session.getUrl());

            // Return the payment intent's client secret for client-side confirmation
            return new PaymentResponse(payment.getPaymentId(), payment.getStatus(), session.getUrl());
        } catch (StripeException e) {
            System.out.println("Error processing payment: " + e.getMessage());
            return PaymentResponse.FailedPayment();
        }
    }

    // Handle successful payments
    public void handlePaymentSucceeded(Event event) {
        // Extract the PaymentIntent object from the event
        Optional<StripeObject> dataObject = event.getDataObjectDeserializer().getObject();
        if (dataObject.isEmpty() || !(dataObject.get() instanceof PaymentIntent paymentIntent))
            throw new InvalidEventDataException("Invalid event data object");

        Long orderId = Long.parseLong(paymentIntent.getMetadata().get("orderId"));

        // Update payment status in the database
        Optional<PaymentEntity> paymentOpt = paymentRepository.findByOrderId(orderId);
        if (paymentOpt.isPresent()) {
            PaymentEntity payment = paymentOpt.get();
            payment.setStatus(PaymentStatus.PAID);
            paymentRepository.save(payment);
        }
    }

    // Handle failed payments
    public void handlePaymentFailed(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
        Long orderId = Long.parseLong(paymentIntent.getMetadata().get("orderId"));

        // Update payment status in the database
        Optional<PaymentEntity> paymentOpt = paymentRepository.findByOrderId(orderId);
        if (paymentOpt.isPresent()) {
            PaymentEntity payment = paymentOpt.get();
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
        }
    }
}

