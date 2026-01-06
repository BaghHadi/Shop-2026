package fr.esiea.shop2026.domain.repository;

import fr.esiea.shop2026.domain.entities.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository {
    Cart save(Cart cart);
    Optional<Cart> findByUserId(UUID userId);
    void deleteById(UUID id);
}