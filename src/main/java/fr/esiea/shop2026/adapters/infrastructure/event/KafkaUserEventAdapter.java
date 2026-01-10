package fr.esiea.shop2026.adapters.infrastructure.event;

import fr.esiea.shop2026.domain.entities.User;
import fr.esiea.shop2026.domain.event.UserEvent;
import fr.esiea.shop2026.domain.repository.UserEventRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaUserEventAdapter implements UserEventRepository {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaUserEventAdapter(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishUserCreated(User user) {
        // 1. On transforme l'entité métier en événement
        UserEvent event = new UserEvent(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().toString()
        );

        // 2. On envoie dans le topic "user-created"
        // La clé est l'ID du user (pour garantir l'ordre dans les partitions)
        System.out.println("📤 Publication de l'événement Kafka pour l'utilisateur  : " + user.getId() + "...");

        try {
            kafkaTemplate.send("user-created", user.getId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        System.err.println("⚠️ Kafka indisponible : événement NON envoyé pour l'utilisateur "
                                + user.getId() + " | Cause = " + ex.getMessage());
                    } else {
                        System.out.println("✅ Événement envoyé pour l'utilisateur " + user.getId() +
                                " | partition=" + result.getRecordMetadata().partition() +
                                " offset=" + result.getRecordMetadata().offset());
                    }
                });
        } catch (Exception e) {
            // Important : Rien ne remonte à l'appelant -> pas d'erreur 500
            System.err.println("⚠️ (User) Exception Kafka : " + e.getMessage());
        }
    }
}