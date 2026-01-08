package fr.esiea.shop2026.domain.entities;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Cart {
    private UUID id;
    private UUID userId; // Lien vers le client
    private List<CartItem> items;

    public Cart(UUID id, UUID userId) {
        this.id = id;
        this.userId = userId;
        this.items = new ArrayList<>();
    }

    public void addItem(Product product, int quantity) {
        this.items.add(new CartItem(product, quantity));
    }

    public BigDecimal calculateTotal() {
        return items.stream()
                .map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public void removeProduct(UUID productId) {
        // Supprime l'item si l'ID du produit correspond
        this.items.removeIf(item -> item.getProduct().getId().equals(productId));
    }
}