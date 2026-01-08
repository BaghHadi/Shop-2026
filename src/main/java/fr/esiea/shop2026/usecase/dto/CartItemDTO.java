package fr.esiea.shop2026.usecase.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CartItemDTO {
    public UUID productId;
    public String productName;
    public int quantity;
    public BigDecimal unitPrice;
    public BigDecimal totalPrice;

    public CartItemDTO(UUID productId, String productName, int quantity, BigDecimal unitPrice, BigDecimal totalPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }
}