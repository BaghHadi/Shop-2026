package fr.esiea.shop2026.domain.repository;

import fr.esiea.shop2026.domain.entities.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(UUID id);
    List<Order> findAll();
}