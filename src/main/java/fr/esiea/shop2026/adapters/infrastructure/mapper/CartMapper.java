package fr.esiea.shop2026.adapters.infrastructure.mapper;

import fr.esiea.shop2026.adapters.infrastructure.entity.CartEntity;
import fr.esiea.shop2026.adapters.infrastructure.entity.CartItemEntity;
import fr.esiea.shop2026.domain.entities.Cart;
import fr.esiea.shop2026.domain.entities.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartEntity toEntity(Cart domain) {
        if (domain == null) return null;

        CartEntity entity = new CartEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());

        // Conversion de la liste d'items
        if (domain.getItems() != null) {
            List<CartItemEntity> itemEntities = domain.getItems().stream()
                    .map(CartMapper::toItemEntity) // Appel de la méthode helper ci-dessous
                    .collect(Collectors.toList());
            entity.setItems(itemEntities);
        }

        return entity;
    }

    // Helper pour convertir un seul item
    private static CartItemEntity toItemEntity(CartItem domainItem) {
        return new CartItemEntity(
                null, // L'ID interne est généré par la BDD
                domainItem.getProduct().getId(),
                domainItem.getQuantity()
        );
    }
}