package com.gostavdev.commercify.productsservice.repositories;

import com.gostavdev.commercify.productsservice.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
