package fr.esiea.shop2026.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// C'est l'objet qui sera transformé en JSON et envoyé dans Kafka
public record OrderEvent(
        UUID orderId,
        UUID userId,
        BigDecimal totalAmount,
        String status,
        LocalDateTime eventDate
) {}