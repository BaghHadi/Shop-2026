package fr.esiea.shop2026.adapters.infrastructure.repository;

import fr.esiea.shop2026.domain.entities.Order;
import fr.esiea.shop2026.domain.repository.OrderEventRepository;
import org.springframework.stereotype.Component;

@Component
public class ConsoleOrderEventAdapter implements OrderEventRepository {

    @Override
    public void publishOrderCreated(Order order) {
        // Pour l'instant, on fait semblant d'envoyer à Kafka
        System.out.println("--------------------------------------------------");
        System.out.println("🔔 [FAUX KAFKA] Événement publié : Commande créée !");
        System.out.println("🆔 ID Commande : " + order.getId());
        System.out.println("💰 Montant : " + order.getTotalAmount());
        System.out.println("--------------------------------------------------");
    }
}