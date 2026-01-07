package fr.esiea.shop2026.domain.service;

import fr.esiea.shop2026.domain.entities.Product;

import java.math.BigDecimal;

public class ProductValidator {

    public void validate(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new RuntimeException("Le nom du produit ne peut pas être vide.");
        }

        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Le prix du produit ne peut pas être négatif.");
        }

        if (product.getStockQuantity() < 0) {
            throw new RuntimeException("La quantité de stock ne peut pas être négative.");
        }
    }
}