package fr.esiea.shop2026.adapters.infrastructure.entity;

import fr.esiea.shop2026.domain.entities.UserEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "users") // Attention "user" est un mot réservé SQL souvent, donc "users"
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    private UUID id; // On génère l'ID dans le code, pas auto par la BDD pour le DDD

    private String firstName;
    private String lastName;

    @Column(unique = true) // L'email doit être unique
    private String email;

    private String password;
    private String deliveryAddress;
    private String phone;

    @Enumerated(EnumType.STRING)
    private UserEnum role;
}