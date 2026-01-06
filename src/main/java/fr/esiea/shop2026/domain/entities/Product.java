package fr.esiea.shop2026.domain.entities;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class Product {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;

    public Product(UUID id, String name, String description, BigDecimal price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public boolean hasStock(int quantityRequested) {
        return this.stockQuantity >= quantityRequested;
    }

    public void reduceStock(int quantity) {
        if (!hasStock(quantity)) {
            throw new RuntimeException("Insufficient stock for product: " + name);
        }
        this.stockQuantity -= quantity;
    }
}