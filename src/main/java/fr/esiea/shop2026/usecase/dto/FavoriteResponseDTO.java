package fr.esiea.shop2026.usecase.dto;

import java.util.List;
import java.util.UUID;

public class FavoriteResponseDTO {
    public UUID id;
    public UUID linkedUserId;
    public List<ProductSummaryDTO> products;

    public FavoriteResponseDTO(UUID id, UUID linkedUserId, List<ProductSummaryDTO> products) {
        this.id = id;
        this.linkedUserId = linkedUserId;
        this.products = products;
    }
}