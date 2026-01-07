package fr.esiea.shop2026.domain.service;

import fr.esiea.shop2026.domain.entities.Order;

import java.math.BigDecimal;

public class OrderValidator {

    public void validate(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new RuntimeException("Impossible de valider une commande sans articles.");
        }

        if (order.getTotalAmount() == null || order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Le montant total de la commande est invalide.");
        }
    }
}