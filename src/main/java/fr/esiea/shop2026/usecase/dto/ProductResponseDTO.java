package fr.esiea.shop2026.usecase.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductResponseDTO {
    public UUID id;
    public String name;
    public String description;
    public BigDecimal price;
    public int stockQuantity;

    // Constructeur pratique pour convertir
    public ProductResponseDTO(UUID id, String name, String description, BigDecimal price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}