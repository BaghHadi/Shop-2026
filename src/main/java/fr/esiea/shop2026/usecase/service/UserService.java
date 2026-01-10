package fr.esiea.shop2026.usecase.service;

import fr.esiea.shop2026.domain.entities.User;
import fr.esiea.shop2026.domain.entities.UserEnum;
import fr.esiea.shop2026.domain.repository.OrderRepository;
import fr.esiea.shop2026.domain.repository.UserEventRepository;
import fr.esiea.shop2026.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEventRepository userEventRepository;

    // Constructeur avec injection des dépendances
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserEventRepository userEventRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userEventRepository = userEventRepository;
    }

    public User createClient(String firstName, String lastName, String email, String password, String deliveryAddress, String phone, UserEnum role) {
        // Hachage du mot de passe ICI avant d'envoyer au repository
        String encodedPassword = passwordEncoder.encode(password);

        // Sauvegarde de l'utilisateur dans la BDD
        User user = userRepository.createClient(firstName, lastName, email, encodedPassword, deliveryAddress, phone, role);

        // Envoie de l'événement Kafka
        userEventRepository.publishUserCreated(user);

        return user;
    }

    public User loginUser(String email, String rawPassword) {
        // 1. On récupère l'utilisateur via l'Adapter (qui ne vérifie plus le MDP)
        User user = userRepository.loginUser(email, rawPassword);

        // 2. C'est ICI qu'on vérifie la sécurité
        // rawPassword = "hadi2004"
        // user.getPassword() = "$2a$10$DnF..." (Le hash en BDD)
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        return user;
    }

    // ... les autres méthodes (getAllUsers, getUserById, removeUser) ne changent pas
    public List<User> getAllUsers() { return userRepository.getAllUsers(); }
    public User getUserById(UUID id) { return userRepository.getUserById(id); }
    public boolean removeUser(UUID id) {
        try { return userRepository.removeUser(getUserById(id)); }
        catch (Exception e) { return false; }
    }
}