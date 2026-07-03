package com.boris.shopinventorycrud.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    private Integer quantity;

    private Double price;

    public enum Status {
        ACTIVE,
        IN_STOCK,
        OUT_OF_STOCK,
        DISCONTINUED
    }

    @Enumerated(EnumType.STRING)
    private Status status;
}