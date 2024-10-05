package com.gostavdev.commercify.productsservice.services;

import com.gostavdev.commercify.productsservice.dto.ProductDTO;
import com.gostavdev.commercify.productsservice.dto.ProductDTOMapper;
import com.gostavdev.commercify.productsservice.entities.ProductEntity;
import com.gostavdev.commercify.productsservice.repositories.ProductRepository;
import com.gostavdev.commercify.productsservice.requests.ProductRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.Product;
import com.stripe.param.ProductCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductDTOMapper mapper;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(mapper).toList();
    }

    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id).map(mapper).orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    public ProductDTO saveProduct(ProductRequest product) throws StripeException {
        long amountInCents = (long) (product.unitPrice() * 100);
        ProductEntity productEntity = new ProductEntity(product);
        ProductEntity savedProduct = productRepository.save(productEntity);

        ProductCreateParams.DefaultPriceData defaultPriceData = ProductCreateParams.DefaultPriceData.builder()
                .setCurrency(product.currency())
                .setUnitAmount(amountInCents).build();

        ProductCreateParams params =
                ProductCreateParams.builder()
                        .setName(product.name())
                        .setDescription(product.description())
                        .setId(productEntity.getId().toString())
                        .setDefaultPriceData(defaultPriceData)
                        .build();
        Product.create(params);

        return mapper.apply(savedProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<ProductDTO> saveProducts(List<ProductRequest> request) throws StripeException {
        return request.stream().map(p -> {
            try {
                return saveProduct(p);
            } catch (StripeException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}
