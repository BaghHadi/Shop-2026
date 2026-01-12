package fr.esiea.shop2026.usecase.service;

import fr.esiea.shop2026.domain.event.UserEvent;
import org.springframework.stereotype.Service;

@Service
public class SendWelcomeEmailUseCase {

    public void sendWelcomeEmail(UserEvent event) {
        // Logique métier : Génération et envoi d'email (simulé)
        System.out.println("===================================================");
        System.out.println("📧 [SERVICE NOTIFICATION] Nouvel utilisateur détecté !");
        System.out.println("👤 Nom       : " + event.firstName() + " " + event.lastName());
        System.out.println("mb Email     : " + event.email());
        System.out.println("📝 Contenu   : 'Bienvenue chez Shop 2026 ! Profitez de nos offres.'");
        System.out.println("🚀 Action    : Email envoyé via SMTP.");
        System.out.println("===================================================");
    }
}