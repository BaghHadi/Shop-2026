package fr.esiea.shop2026.adapters.infrastructure.event;

import fr.esiea.shop2026.domain.entities.Order;
import fr.esiea.shop2026.domain.event.OrderEvent;
import fr.esiea.shop2026.domain.repository.OrderEventRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class KafkaOrderEventAdapter implements OrderEventRepository {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaOrderEventAdapter(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishOrderCreated(Order order) {
        // 1. On transforme l'entité métier en événement (DTO)
        OrderEvent event = new OrderEvent(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                "CREATED",
                LocalDateTime.now()
        );

        // 2. On envoie dans le topic "order-created"
        // La clé est l'ID de commande (pour garantir l'ordre dans les partitions)
        System.out.println("📤 Publication de l'événement Kafka pour la commande : " + order.getId());
        kafkaTemplate.send("order-created", order.getId().toString(), event);
    }
}