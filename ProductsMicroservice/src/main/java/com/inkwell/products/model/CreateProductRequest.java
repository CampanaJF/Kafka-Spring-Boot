package com.inkwell.products.model;

import java.math.BigDecimal;

public record CreateProductRequest(
        String title,
        BigDecimal price,
        Integer quantity

) {
}
