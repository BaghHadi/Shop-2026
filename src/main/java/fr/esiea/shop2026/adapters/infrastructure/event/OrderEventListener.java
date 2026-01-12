package fr.esiea.shop2026.adapters.infrastructure.event;

import fr.esiea.shop2026.domain.event.OrderEvent;
import fr.esiea.shop2026.usecase.service.ProcessShippingUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final ProcessShippingUseCase processShippingUseCase;

    public OrderEventListener(ProcessShippingUseCase processShippingUseCase) {
        this.processShippingUseCase = processShippingUseCase;
    }

    // On écoute le même topic que celui dans ton KafkaOrderEventAdapter ("order-created")
    @KafkaListener(topics = "order-created", groupId = "shop-group")
    public void listen(OrderEvent event) {
        System.out.println("📨 [KAFKA] Message reçu pour la commande " + event.orderId());

        // Appel du métier
        processShippingUseCase.processShipping(event);
    }
}