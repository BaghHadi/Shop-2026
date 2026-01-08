package fr.esiea.shop2026.usecase.dto;

import java.util.UUID;

public class AddToCartDTO {
    public UUID userId;
    public UUID productId;
    public int quantity;
}