package com.gostavdev.commercify.orderservice.feignclients;

import com.gostavdev.commercify.orderservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("http://localhost:8080/api/v1/products")
public interface ProductsClient {
    @RequestMapping(method = RequestMethod.GET, value = "/{productId}")
    ProductDto getProduct(@PathVariable("productId") Long productId);
}