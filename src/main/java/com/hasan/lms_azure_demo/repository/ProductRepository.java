package com.hasan.lms_azure_demo.repository;


import com.hasan.lms_azure_demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
