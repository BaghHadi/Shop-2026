package fr.esiea.shop2026.domain.entities;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class User {
    String id;
    String firstName;
    String lastName;
    String email;
    String password;
    String deliveryAddress;
    String phone;
    UserEnum role;  // Standard, Premium or Administrator

    public User() {
        this.id = null;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.password = null;
        this.deliveryAddress = null;
        this.phone = null;
        this.role = null;
    }

    public User(String id, String firstName, String lastName, String email, String password, String deliveryAddress, String phone, UserEnum role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.deliveryAddress = deliveryAddress;
        this.phone = phone;
        this.role = role;
    }
}
