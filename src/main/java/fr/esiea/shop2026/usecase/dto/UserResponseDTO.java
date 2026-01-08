package fr.esiea.shop2026.usecase.dto;

import fr.esiea.shop2026.domain.entities.UserEnum;

import java.util.UUID;

public class UserResponseDTO {
    public UUID id;
    public String firstName;
    public String lastName;
    public String email;
    public String deliveryAddress;
    public String phone;
    public UserEnum role;

    public UserResponseDTO(UUID id, String firstName, String lastName, String email, String deliveryAddress, String phone, UserEnum role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.deliveryAddress = deliveryAddress;
        this.phone = phone;
        this.role = role;
    }
}