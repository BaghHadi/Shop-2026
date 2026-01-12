package fr.esiea.shop2026.adapters.infrastructure.event;

import fr.esiea.shop2026.domain.event.UserEvent;
import fr.esiea.shop2026.usecase.service.SendWelcomeEmailUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

    private final SendWelcomeEmailUseCase sendWelcomeEmailUseCase;

    public UserEventListener(SendWelcomeEmailUseCase sendWelcomeEmailUseCase) {
        this.sendWelcomeEmailUseCase = sendWelcomeEmailUseCase;
    }

    // On écoute le topic "user-created"
    @KafkaListener(topics = "user-created", groupId = "shop-group")
    public void listen(UserEvent event) {
        System.out.println("📨 [KAFKA LISTENER] Inscription reçue pour : " + event.email());

        // Appel du métier
        sendWelcomeEmailUseCase.sendWelcomeEmail(event);
    }
}