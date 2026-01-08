package fr.esiea.shop2026;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.UUID;

public class ShopConsoleClient {

    // Configuration
    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Scanner scanner = new Scanner(System.in);

    // Session Utilisateur Courante
    private static UUID currentUserId = null;
    private static String currentUserRole = null; // "CLIENT" ou "SELLER"
    private static String currentUserName = null;

    // Couleurs pour la console
    private static final String RESET = "\033[0m";
    private static final String RED = "\033[0;31m";
    private static final String GREEN = "\033[0;32m";
    private static final String YELLOW = "\033[0;33m";
    private static final String CYAN = "\033[0;36m";
    private static final String BLUE_BOLD = "\033[1;34m";

    public static void main(String[] args) {
        System.out.println(BLUE_BOLD + "==========================================" + RESET);
        System.out.println(BLUE_BOLD + "       BIENVENUE SUR SHOP 2026            " + RESET);
        System.out.println(BLUE_BOLD + "==========================================" + RESET);

        while (true) {
            if (currentUserId == null) {
                authMenu();
            } else {
                if ("SELLER".equals(currentUserRole)) {
                    sellerMenu();
                } else {
                    clientMenu();
                }
            }
        }
    }

    // ==========================================
    // 🔐 AUTHENTIFICATION
    // ==========================================
    private static void authMenu() {
        System.out.println(CYAN + "\n--- CONNEXION / INSCRIPTION ---" + RESET);
        System.out.println("1. Se connecter");
        System.out.println("2. S'inscrire");
        System.out.println("0. Quitter");
        System.out.print("Votre choix : ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> login();
            case "2" -> register();
            case "0" -> {
                System.out.println("Au revoir !");
                System.exit(0);
            }
            default -> System.out.println(RED + "Choix invalide." + RESET);
        }
    }

    private static void login() {
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String pwd = scanner.nextLine();

        // Création manuelle du JSON pour login
        String json = String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, pwd);
        JsonNode response = sendRequest("POST", "/users/login", json);

        if (response != null) {
            currentUserId = UUID.fromString(response.get("id").asText());
            currentUserRole = response.get("role").asText(); // Suppose que le JSON renvoie "role"
            currentUserName = response.get("firstName").asText();
            System.out.println(GREEN + "✅ Bonjour " + currentUserName + " (" + currentUserRole + ") !" + RESET);
        }
    }

    private static void register() {
        System.out.println("\n--- NOUVEAU COMPTE ---");
        System.out.print("Prénom : "); String fname = scanner.nextLine();
        System.out.print("Nom : "); String lname = scanner.nextLine();
        System.out.print("Email : "); String email = scanner.nextLine();
        System.out.print("Mot de passe : "); String pwd = scanner.nextLine();
        System.out.print("Adresse : "); String addr = scanner.nextLine();
        System.out.print("Téléphone : "); String phone = scanner.nextLine();

        System.out.println("Rôle :");
        System.out.println("  1. Client");
        System.out.println("  2. Vendeur (Seller)");
        System.out.print("Choix : ");
        String roleChoice = scanner.nextLine();
        String role = "CLIENT";
        if ("2".equals(roleChoice)) role = "SELLER";

        String json = String.format(
                "{\"firstName\":\"%s\", \"lastName\":\"%s\", \"email\":\"%s\", \"password\":\"%s\", \"address\":\"%s\", \"phone\":\"%s\", \"role\":\"%s\"}",
                fname, lname, email, pwd, addr, phone, role
        );

        sendRequest("POST", "/users/register", json);
        System.out.println(GREEN + "Compte créé avec succès ! Veuillez vous connecter." + RESET);
    }

    private static void logout() {
        currentUserId = null;
        currentUserRole = null;
        currentUserName = null;
        System.out.println(YELLOW + "👋 Vous êtes déconnecté." + RESET);
    }

    // ==========================================
    // 💼 MENU VENDEUR (SELLER)
    // ==========================================
    private static void sellerMenu() {
        System.out.println(CYAN + "\n=== ESPACE VENDEUR (" + currentUserName + ") ===" + RESET);
        System.out.println("1. 📦 Voir mes produits");
        System.out.println("2. ➕ Ajouter un produit");
        System.out.println("3. ✏️ Modifier un produit");
        System.out.println("4. 🗑️ Supprimer un produit");
        System.out.println("---------------------------");
        System.out.println("5. 📜 Voir TOUTES les commandes");
        System.out.println("6. ✅ Valider une commande");
        System.out.println("---------------------------");
        System.out.println("9. 🚪 Déconnexion");
        System.out.print("Choix : ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> sendRequest("GET", "/products", null);
            case "2" -> createProduct();
            case "3" -> updateProduct();
            case "4" -> deleteProduct();
            case "5" -> sendRequest("GET", "/orders", null);
            case "6" -> validateOrder();
            case "9" -> logout();
            default -> System.out.println(RED + "Option invalide." + RESET);
        }
    }

    private static void createProduct() {
        try {
            System.out.print("Nom du produit : "); String name = scanner.nextLine();
            System.out.print("Description : "); String desc = scanner.nextLine();
            System.out.print("Prix (ex: 10.50) : "); double price = Double.parseDouble(scanner.nextLine());
            System.out.print("Quantité stock : "); int stock = Integer.parseInt(scanner.nextLine());

            String json = String.format(
                    "{\"name\":\"%s\", \"description\":\"%s\", \"price\":%s, \"stockQuantity\":%d}",
                    name, desc, price, stock
            );
            sendRequest("POST", "/products", json);
        } catch (Exception e) {
            System.out.println(RED + "Erreur de saisie (vérifiez le prix/stock)." + RESET);
        }
    }

    private static void updateProduct() {
        try {
            System.out.print("ID du produit à modifier : "); String id = scanner.nextLine();
            System.out.println("--- Nouvelles infos ---");
            System.out.print("Nom : "); String name = scanner.nextLine();
            System.out.print("Description : "); String desc = scanner.nextLine();
            System.out.print("Prix : "); double price = Double.parseDouble(scanner.nextLine());
            System.out.print("Quantité stock : "); int stock = Integer.parseInt(scanner.nextLine());

            String json = String.format(
                    "{\"name\":\"%s\", \"description\":\"%s\", \"price\":%s, \"stockQuantity\":%d}",
                    name, desc, price, stock
            );
            sendRequest("PUT", "/products/" + id, json);
        } catch (Exception e) {
            System.out.println(RED + "Erreur de saisie." + RESET);
        }
    }

    private static void deleteProduct() {
        System.out.print("ID du produit à supprimer : ");
        String id = scanner.nextLine();
        sendRequest("DELETE", "/products/" + id, null);
    }

    private static void validateOrder() {
        System.out.print("ID de la commande à valider : ");
        String id = scanner.nextLine();
        sendRequest("PATCH", "/orders/" + id + "/validate", null);
    }

    // ==========================================
    // 🛒 MENU CLIENT (BUYER)
    // ==========================================
    private static void clientMenu() {
        System.out.println(CYAN + "\n=== ESPACE CLIENT (" + currentUserName + ") ===" + RESET);
        System.out.println("1. 🛍️ Catalogue Produits");
        System.out.println("2. 🛒 Mon Panier");
        System.out.println("3. ❤️ Mes Favoris");
        System.out.println("4. 💳 Passer Commande");
        System.out.println("5. 📜 Mes Commandes (Historique)");
        System.out.println("9. 🚪 Déconnexion");
        System.out.print("Choix : ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> sendRequest("GET", "/products", null);
            case "2" -> cartMenu();
            case "3" -> favoritesMenu();
            case "4" -> sendRequest("POST", "/orders/" + currentUserId, null);
            case "5" -> System.out.println(YELLOW + "Note: Pour voir 'Mes' commandes, on pourrait filtrer, mais voici tout l'historique pour l'instant :" + RESET + "\n" + sendRequest("GET", "/orders", null));
            case "9" -> logout();
            default -> System.out.println(RED + "Option invalide." + RESET);
        }
    }

    private static void cartMenu() {
        System.out.println(CYAN + "\n--- MON PANIER ---" + RESET);
        sendRequest("GET", "/carts/" + currentUserId, null);

        System.out.println("\nActions :");
        System.out.println("1. Ajouter un produit");
        System.out.println("2. Retirer un produit");
        System.out.println("0. Retour");
        System.out.print("Choix : ");

        String subChoice = scanner.nextLine();
        if ("1".equals(subChoice)) {
            try {
                System.out.print("ID Produit : "); String pid = scanner.nextLine();
                System.out.print("Quantité : "); int qty = Integer.parseInt(scanner.nextLine());
                String json = String.format("{\"userId\":\"%s\", \"productId\":\"%s\", \"quantity\":%d}", currentUserId, pid, qty);
                sendRequest("POST", "/carts/add", json);
            } catch (Exception e) { System.out.println(RED + "Erreur de saisie" + RESET); }
        } else if ("2".equals(subChoice)) {
            System.out.print("ID Produit à retirer : "); String pid = scanner.nextLine();
            sendRequest("DELETE", "/carts/" + currentUserId + "/items/" + pid, null);
        }
    }

    private static void favoritesMenu() {
        System.out.println(CYAN + "\n--- MES FAVORIS ---" + RESET);
        // ✅ URL CORRIGÉE ICI : /favorites/user/{id}
        sendRequest("GET", "/favorites/user/" + currentUserId, null);

        System.out.println("\nActions :");
        System.out.println("1. Ajouter un produit aux favoris");
        System.out.println("2. Retirer un produit des favoris");
        System.out.println("0. Retour");
        System.out.print("Choix : ");

        String subChoice = scanner.nextLine();
        if ("1".equals(subChoice)) {
            System.out.print("ID Produit : "); String pid = scanner.nextLine();
            // Récupérer l'ID de la liste de favoris est complexe ici sans faire un GET avant.
            // Pour simplifier, on suppose que le user a déjà une liste.
            // Astuce : On récupère d'abord la liste pour avoir son ID
            JsonNode favList = sendRequest("GET", "/favorites/user/" + currentUserId, null);
            if (favList != null && favList.has("id")) {
                String favId = favList.get("id").asText();
                sendRequest("POST", "/favorites/" + favId + "/products/" + pid, null);
            } else {
                // Si pas de liste, on la crée
                JsonNode newList = sendRequest("POST", "/favorites/user/" + currentUserId, null);
                if(newList != null) {
                    String newId = newList.get("id").asText();
                    sendRequest("POST", "/favorites/" + newId + "/products/" + pid, null);
                }
            }
        } else if ("2".equals(subChoice)) {
            System.out.print("ID Produit : "); String pid = scanner.nextLine();
            JsonNode favList = sendRequest("GET", "/favorites/user/" + currentUserId, null);
            if (favList != null && favList.has("id")) {
                String favId = favList.get("id").asText();
                sendRequest("DELETE", "/favorites/" + favId + "/products/" + pid, null);
            }
        }
    }

    // ==========================================
    // 📡 MOTEUR HTTP (Générique)
    // ==========================================
    private static JsonNode sendRequest(String method, String endpoint, String jsonBody) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Content-Type", "application/json");

            switch (method) {
                case "GET" -> builder.GET();
                case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(jsonBody == null ? "" : jsonBody));
                case "PUT" -> builder.PUT(HttpRequest.BodyPublishers.ofString(jsonBody == null ? "" : jsonBody));
                case "DELETE" -> builder.DELETE();
                case "PATCH" -> builder.method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody == null ? "" : jsonBody));
            }

            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                if (response.body() != null && !response.body().isEmpty()) {
                    JsonNode node = mapper.readTree(response.body());
                    // Affichage joli du JSON
                    System.out.println(GREEN + "✅ Réponse :" + RESET);
                    System.out.println(node.toPrettyString());
                    return node;
                }
                System.out.println(GREEN + "✅ Succès (Aucun contenu)" + RESET);
                return null;
            } else {
                System.out.println(RED + "❌ Erreur " + response.statusCode() + ":" + RESET);
                System.out.println(response.body());
                return null;
            }
        } catch (Exception e) {
            System.out.println(RED + "⚠️ Erreur de connexion : " + e.getMessage() + RESET);
            return null;
        }
    }
}