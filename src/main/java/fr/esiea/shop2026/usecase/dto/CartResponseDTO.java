package fr.esiea.shop2026.usecase.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CartResponseDTO {
    public UUID cartId;
    public UUID userId;
    public List<CartItemDTO> items;
    public BigDecimal totalAmount;

    public CartResponseDTO(UUID cartId, UUID userId, List<CartItemDTO> items, BigDecimal totalAmount) {
        this.cartId = cartId;
        this.userId = userId;
        this.items = items;
        this.totalAmount = totalAmount;
    }
}