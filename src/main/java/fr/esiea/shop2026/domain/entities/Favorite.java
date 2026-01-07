package fr.esiea.shop2026.domain.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class Favorite {

    UUID id;
    User linkedUser;
    List<Product> products;

    public Favorite() {
        this.linkedUser = null;
        this.products = null;
    }

    public Favorite(UUID id, User linkedUser, List<Product> products) {
        this.id = id;
        this.linkedUser = linkedUser;
        this.products = products;
    }
}
