package fr.esiea.shop2026.domain.repository;

import fr.esiea.shop2026.domain.entities.Order;

public interface OrderEventRepository {
    void publishOrderCreated(Order order);
}