package fr.esiea.shop2026.domain.entities;

public enum UserEnum {

    // Classic client
    CLIENT("Client"),
    // Seller
    SELLER("Seller");

    private final String status;

    private UserEnum(String status) {
        this.status = status;
    }

    public String getStatus () {
        return this.status;
    }
}
