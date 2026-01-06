package fr.esiea.shop2026.domain.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Favorite {

    String id;
    User linkedUser;
    List<Product> products;

    public Favorite() {
        this.linkedUser = null;
        this.products = null;
    }

    public Favorite(User linkedUser, List<Product> products) {
        this.linkedUser = linkedUser;
        this.products = products;
    }
}
