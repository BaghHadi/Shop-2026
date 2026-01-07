package fr.esiea.shop2026.domain.service;

public class CartValidator {

    public void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new RuntimeException("La quantité ajoutée au panier doit être supérieure à 0.");
        }
    }
}