package fr.esiea.shop2026.domain.repository;

import fr.esiea.shop2026.domain.entities.User;

public interface UserEventRepository {
    void publishUserCreated(User user);
}
