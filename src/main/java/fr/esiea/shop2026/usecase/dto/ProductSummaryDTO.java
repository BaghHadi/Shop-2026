package fr.esiea.shop2026.usecase.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductSummaryDTO {
    public UUID id;
    public String name;
    public BigDecimal price;

    public ProductSummaryDTO(UUID id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}