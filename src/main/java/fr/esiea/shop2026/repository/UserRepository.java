package fr.esiea.shop2026.repository;

import fr.esiea.shop2026.domain.entities.User;
import fr.esiea.shop2026.domain.entities.UserEnum;

import java.util.List;

public interface UserRepository {

    // Create a User (Client or Seller)
    User createClient(String firstName, String lastName, String email, String password, String deliveryAddress, String phone, UserEnum role);

    // Log in a user (Standard, Premium or Administrator)
    User loginUser(String email, String password);

    // Get all users from DB
    List<User> getAllUsers();

    // Gey a user by its ID
    User getUserById(String userID);

    // Remove a user from DB
    boolean removeUser(User user);
}
