package com.gostavdev.commercify.productsservice.repositories;

import com.gostavdev.commercify.productsservice.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> queryAllByActiveTrue();
}
