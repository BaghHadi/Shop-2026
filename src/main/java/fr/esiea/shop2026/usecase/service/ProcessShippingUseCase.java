package fr.esiea.shop2026.usecase.service;

import fr.esiea.shop2026.domain.event.OrderEvent;
import org.springframework.stereotype.Service;

@Service
public class ProcessShippingUseCase {

    public void processShipping(OrderEvent event) {
        // Logique métier : Simulation de préparation de commande
        System.out.println("===================================================");
        System.out.println("📦 [LOGISTIQUE] Nouvelle commande détectée !");
        System.out.println("📝 ID Commande : " + event.orderId());
        System.out.println("👤 Client      : " + event.userId());
        System.out.println("💰 Montant     : " + event.totalAmount() + " €");
        System.out.println("🚚 Action      : Étiquette d'expédition générée.");
        System.out.println("===================================================");
    }
}