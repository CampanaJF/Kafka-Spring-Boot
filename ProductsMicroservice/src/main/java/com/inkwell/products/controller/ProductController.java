package com.inkwell.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inkwell.products.model.CreateProductRequest;
import com.inkwell.products.model.ErrorMessage;
import com.inkwell.products.model.Product;
import com.inkwell.products.service.ProductService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService service;

    private final ObjectMapper mapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/async")
    public ResponseEntity<String> createProductAsync(@RequestBody CreateProductRequest product){

        String productId = service.createProductAsync(mapper.convertValue(product, Product.class));

        return new ResponseEntity<String>(productId,HttpStatus.CREATED);
    }

    @PostMapping("/sync")
    public ResponseEntity<Object> createProductSync(@RequestBody CreateProductRequest product){

        String productId;

        try {
            productId = service.createProductSync(mapper.convertValue(product, Product.class));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(new ErrorMessage(new Date(),e.getMessage(),
                    "products/sync"),HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return new ResponseEntity<>(productId,HttpStatus.CREATED);
    }
}
