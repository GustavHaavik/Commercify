package com.gostavdev.commercify.orderservice.model;

import com.gostavdev.commercify.orderservice.dto.ProductDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "order_lines")
@NoArgsConstructor
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderline_id", nullable = false)
    private Long orderlineId;

    @Column(name = "product_id", nullable = false, updatable = false)
    private Long productId;
    @Column(name = "quantity", nullable = false, updatable = false)
    private Integer quantity;
    @Column(name = "unit_price", nullable = false, updatable = false)
    private Double unitPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private Order order;

    @Transient
    private ProductDto product;
}
