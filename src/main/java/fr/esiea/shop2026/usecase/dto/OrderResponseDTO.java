package fr.esiea.shop2026.usecase.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderResponseDTO {
    public UUID id;
    public UUID userId;
    public BigDecimal totalAmount;
    public String status;
    public LocalDateTime createdAt;
    public List<OrderItemDTO> items; // On utilise bien le DTO ici, pas l'entité !

    public OrderResponseDTO(UUID id, UUID userId, BigDecimal totalAmount, String status, LocalDateTime createdAt, List<OrderItemDTO> items) {
        this.id = id;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.items = items;
    }
}