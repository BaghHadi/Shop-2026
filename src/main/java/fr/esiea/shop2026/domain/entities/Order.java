package fr.esiea.shop2026.domain.entities;

import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Order {
    private UUID id;
    private UUID userId;
    private List<CartItem> items;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;

    public Order(UUID id, UUID userId, List<CartItem> items, BigDecimal totalAmount) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public void validateOrder() {
        this.status = "VALIDATED";
    }

    public enum OrderStatus {
        PENDING, VALIDATED, SHIPPED, CANCELLED
    }
}