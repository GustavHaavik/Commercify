package com.gostavdev.commercify.orderservice.repositories;

import com.gostavdev.commercify.orderservice.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
}