package fr.esiea.shop2026.domain.event;

import java.util.UUID;

// C'est l'objet qui sera transformé en JSON et envoyé dans Kafka
public record UserEvent(
    UUID id,
    String firstName,
    String lastName,
    String email,
    String role
) {}