package fr.esiea.shop2026.adapters.infrastructure.repository;

import fr.esiea.shop2026.adapters.infrastructure.entity.UserEntity;
import fr.esiea.shop2026.adapters.infrastructure.repository.SpringDataUserRepository;
import fr.esiea.shop2026.domain.entities.User;
import fr.esiea.shop2026.domain.entities.UserEnum;

import fr.esiea.shop2026.domain.exception.NotFoundException;
import fr.esiea.shop2026.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository jpaRepository;

    public UserRepositoryAdapter(SpringDataUserRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User createClient(String firstName, String lastName, String email, String password, String deliveryAddress, String phone, UserEnum role) {
        // 1. Création de l'entité JPA
        UserEntity entity = new UserEntity();
        entity.setId(UUID.randomUUID());
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setEmail(email);
        entity.setPassword(password); // Penser à hacher le mot de passe dans le Service avant
        entity.setDeliveryAddress(deliveryAddress);
        entity.setPhone(phone);
        entity.setRole(role);

        // 2. Sauvegarde
        UserEntity savedEntity = jpaRepository.save(entity);

        // 3. Retour au domaine
        return toDomain(savedEntity);
    }

    @Override
    public User loginUser(String email, String password) {
        // 1. On cherche juste l'utilisateur par email
        UserEntity entity = jpaRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        // C'est le Service qui va comparer le hash avec passwordEncoder.

        return toDomain(entity);
    }

    @Override
    public List<User> getAllUsers() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(UUID userID) {
        return jpaRepository.findById(userID)
                .map(this::toDomain)
                .orElseThrow(() -> new NotFoundException("User", userID));
    }

    @Override
    public boolean removeUser(User user) {
        if (user != null && user.getId() != null && jpaRepository.existsById(user.getId())) {
            jpaRepository.deleteById(user.getId());
            return true;
        }
        return false;
    }

    // --- Mapper Privé (Entity -> Domain) ---
    private User toDomain(UserEntity e) {
        return new User(
                e.getId(),
                e.getFirstName(),
                e.getLastName(),
                e.getEmail(),
                e.getPassword(),
                e.getDeliveryAddress(),
                e.getPhone(),
                e.getRole()
        );
    }
}