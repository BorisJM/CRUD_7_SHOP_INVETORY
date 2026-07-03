package com.boris.shopinventorycrud.repository;

import com.boris.shopinventorycrud.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByName(String name);

    List<Product> findByQuantityGreaterThan(Integer quantity);

    List<Product> findByQuantityLessThan(Integer quantity);

    List<Product> findByStatus(Product.Status status);

    List<Product> findByPriceGreaterThan(Double price);

    List<Product> findByCategoryContaining(String category);
}