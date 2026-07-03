package com.boris.shopinventorycrud.service;

import com.boris.shopinventorycrud.entity.Product;
import com.boris.shopinventorycrud.repository.ProductRepository;
import com.boris.shopinventorycrud.exception.ProductNotFoundException;
import com.boris.shopinventorycrud.websocket.ProductNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductNotificationService notificationService;
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAllAvailableProducts() {
        return productRepository.findByQuantityGreaterThan(0);
    }

    public List<Product> getAllAvailableProductsByStatus(Product.Status status) {
        return productRepository.findByStatus(status);
    }

    public List<Product> getAllLowStockProducts(){
        return productRepository.findByQuantityLessThan(10);
    }

    public Product getMostExpensiveProduct(){
        return productRepository.findAll().stream().max(Comparator.comparing(Product::getPrice)).orElse(null);
    }

    public Product getCheapestProduct(){
        return productRepository.findAll().stream().min(Comparator.comparing(Product::getPrice)).orElse(null);
    }

    public Double getAverageProductsPrice(){
        return productRepository.findAll().stream().mapToDouble(Product::getPrice).average().orElse(0.0);
    }

    public Double getTotalStockValue() {
        return productRepository.findAll().stream().mapToDouble(product -> product.getQuantity() * product.getPrice()).sum();
    }

    public List<Product> findByCategory(String category){
        return productRepository.findByCategoryContaining(category);
    }

    public List<Product> getProductsWithPriceGreaterThan(Double price){
        return  productRepository.findByPriceGreaterThan(price);
    }

    public List<Product> getProductsSortedByStockValue(){
        return productRepository.findAll().stream().sorted(Comparator.comparingDouble(product -> product.getQuantity() * product.getPrice())).collect(Collectors.toList());
    }

    public List<Product> getProductsOverTwentyStockValue(){
        Double stockValue =  this.getTotalStockValue();
        return productRepository.findAll().stream().filter(product -> ((product.getQuantity() * product.getPrice()) / stockValue) > 0.2).toList();
    }

    public List<Product> getElectricalDevices(){
        return productRepository.findByCategoryContaining("Electrical-devices");
    }

    public List<Product> getProductsByQuantity(Integer quantity) {
        return productRepository.findByQuantityGreaterThan(quantity);
    }

    public List<Product> getProductsWithHighQuantity() {

        return productRepository.findAll()
                .stream()
                .filter(product -> product.getQuantity() > 10)
                .sorted(Comparator.comparing(Product::getQuantity).reversed())
                .toList();
    }

    public Product createProduct(Product product) {

        Product savedProduct = productRepository.save(product);

        notificationService.sendProductAddedNotification(
                savedProduct.getName()
        );

        return savedProduct;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product updateProduct(Long id, Product updatedProduct) {

        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setCategory(updatedProduct.getCategory());
                    product.setPrice(updatedProduct.getPrice());
                    product.setQuantity(updatedProduct.getQuantity());
                    product.setStatus(updatedProduct.getStatus());

                    return productRepository.save(product);
                })
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}