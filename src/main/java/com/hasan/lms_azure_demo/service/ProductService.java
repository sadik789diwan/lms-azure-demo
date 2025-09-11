package com.hasan.lms_azure_demo.service;

import com.hasan.lms_azure_demo.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductService {
    Product addProduct(Product product);
    List<Product> getAllProducts();
    Product getProductById(Long id);
    void deleteProduct(Long id);
}

