package fr.esiea.shop2026.adapters.infrastructure.repository;

import fr.esiea.shop2026.adapters.infrastructure.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataCartRepository extends JpaRepository<CartEntity, UUID> {
    //car de base il fais avec Id du cart mais la c'est un find qui le genere pas tt suel
    Optional<CartEntity> findByUserId(UUID userId);
}