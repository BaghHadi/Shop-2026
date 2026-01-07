package fr.esiea.shop2026.usecase.service;

import fr.esiea.shop2026.domain.entities.User;
import fr.esiea.shop2026.domain.entities.UserEnum;
import fr.esiea.shop2026.repository.UserRepository;

import java.util.List;
import java.util.UUID;

public class UserService {

    public UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    
    public User createClient(String firstName, String lastName, String email, String password, String deliveryAddress, String phone, UserEnum role) {
        return null;
    }

    
    public User loginUser(String email, String password) {
        return null;
    }

    
    public List<User> getAllUsers() {
        return List.of();
    }

    
    public User getUserById(UUID userID) {
        return null;
    }

    
    public boolean removeUser(User user) {
        return false;
    }
}
