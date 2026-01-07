package fr.esiea.shop2026.adapters.infrastructure.persistence.repository;

import fr.esiea.shop2026.adapters.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, UUID> {
}