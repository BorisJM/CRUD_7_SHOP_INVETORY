package com.boris.shopinventorycrud.dto;

import com.boris.shopinventorycrud.entity.Product;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {

    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotBlank(message = "Category cannot be empty")
    private String category;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 100, message = "Quantity must be max 100")
    private Integer quantity;

    @DecimalMin(value = "1.00", message = "The price should be at least 1.00$")
    private Double price;

    private Product.Status status;
}