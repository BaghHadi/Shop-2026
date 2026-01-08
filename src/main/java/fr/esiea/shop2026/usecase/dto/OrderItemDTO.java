package fr.esiea.shop2026.usecase.dto;

import java.math.BigDecimal;

public class OrderItemDTO {
    public String productName;
    public int quantity;
    public BigDecimal unitPrice;
    public BigDecimal subTotal;

    public OrderItemDTO(String productName, int quantity, BigDecimal unitPrice, BigDecimal subTotal) {
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subTotal = subTotal;
    }
}