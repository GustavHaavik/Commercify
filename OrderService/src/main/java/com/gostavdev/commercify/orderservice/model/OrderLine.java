package com.gostavdev.commercify.orderservice.model;

import com.gostavdev.commercify.orderservice.dto.ProductDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "order_lines")
@NoArgsConstructor
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderline_id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;
    private Integer quantity;

    @Transient
    private ProductDto product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
