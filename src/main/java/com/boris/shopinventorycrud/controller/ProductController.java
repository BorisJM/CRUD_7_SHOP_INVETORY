package com.boris.shopinventorycrud.controller;

import jakarta.validation.Valid;
import com.boris.shopinventorycrud.dto.ProductRequestDto;
import com.boris.shopinventorycrud.entity.Product;
import com.boris.shopinventorycrud.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.atmosphere.config.service.Get;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/available")
    public List<Product> getAllAvailableProducts() {
        return productService.getAllAvailableProducts();
    }

    @GetMapping("/low-stock")
    public List<Product> getAllLowStockProducts() {
        return productService.getAllLowStockProducts();
    }

    @GetMapping("/prices/most-expensive")
    public Product getMostExpensiveProduct() {
        return productService.getMostExpensiveProduct();
    }

    @GetMapping("/prices/cheapest")
    public Product getCheapestProduct() {
        return productService.getCheapestProduct();
    }

    @GetMapping("/prices/average-price")
    public Double getAverageProductsPrice(){
        return productService.getAverageProductsPrice();
    }

    @GetMapping("/total-stock-value")
    public Double getTotalStockValue(){
        return productService.getTotalStockValue();
    }

    @GetMapping("/find-by-category/{category}")
    public List<Product> findByCategory(@PathVariable String category){
        return productService.findByCategory(category);
    }

    @GetMapping("/categories/Electrical-devices")
    public List<Product> getElectricalDevices(){
        return productService.getElectricalDevices();
    }

    @GetMapping("/prices/price-greater-than/{price}")
    public List<Product> getProductsWithPriceGreaterThan(@PathVariable Double price){
        return productService.getProductsWithPriceGreaterThan(price);
    }

    @GetMapping("/sorted/by-stock-value")
    public List<Product> getProductsSortedByStockValue(){
        return productService.getProductsSortedByStockValue();
    }

    @GetMapping("/over-twenty-stock-value")
    public List<Product> getProductsOverTwentyStockValue(){
        return productService.getProductsOverTwentyStockValue();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/quantity-greater-than/{quantity}")
    public List<Product> getProductsByQuantity(@PathVariable Integer quantity) {
        return productService.getProductsByQuantity(quantity);
    }

    @PostMapping
    public Product createProduct(@Valid @RequestBody ProductRequestDto dto) {

        Product product = Product.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .status(dto.getStatus())
                .build();

        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id,
                               @RequestBody Product product) {

        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/highest-quantity")
    public List<Product> getProductsWithHighQuantity() {
        return productService.getProductsWithHighQuantity();
    }
}