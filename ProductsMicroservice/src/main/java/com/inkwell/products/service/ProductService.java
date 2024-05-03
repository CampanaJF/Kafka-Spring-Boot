package com.inkwell.products.service;

import com.inkwell.products.model.Product;

public interface ProductService {

    String createProductAsync(Product product);

    String createProductSync(Product product) throws Exception;
}
