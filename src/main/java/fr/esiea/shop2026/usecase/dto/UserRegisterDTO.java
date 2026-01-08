package fr.esiea.shop2026.usecase.dto;

import fr.esiea.shop2026.domain.entities.UserEnum;

public class UserRegisterDTO {
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String address; // Mapped vers deliveryAddress
    public String phone;
    public UserEnum role; // CLIENT ou SELLER
}