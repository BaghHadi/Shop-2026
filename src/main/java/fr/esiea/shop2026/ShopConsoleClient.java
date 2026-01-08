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

    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Scanner scanner = new Scanner(System.in);

    // UUID de l'utilisateur courant (simulé)
    private static UUID currentUserId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

    // Couleurs pour faire joli
    private static final String RESET = "\033[0m";
    private static final String RED = "\033[0;31m";
    private static final String GREEN = "\033[0;32m";
    private static final String YELLOW = "\033[0;33m";
    private static final String CYAN = "\033[0;36m";
    private static final String BLUE_BOLD = "\033[1;34m";

    public static void main(String[] args) {
        System.out.println(BLUE_BOLD + "==========================================" + RESET);
        System.out.println(BLUE_BOLD + "    BIENVENUE DANS LE SHOP ESIEA 2026     " + RESET);
        System.out.println(BLUE_BOLD + "==========================================" + RESET);

        System.out.print("Entrez votre ID Utilisateur (Entrée pour défaut): ");
        String inputId = scanner.nextLine();
        if (!inputId.isBlank()) {
            try {
                currentUserId = UUID.fromString(inputId);
            } catch (Exception e) {
                System.out.println(RED + "ID invalide, utilisation de l'ID par défaut." + RESET);
            }
        }
        System.out.println(GREEN + "Connecté en tant que : " + currentUserId + RESET);

        while (true) {
            printMainMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> menuProducts();
                case "2" -> menuCart();
                case "3" -> menuOrders();
                case "9" -> changeUser();
                case "0" -> {
                    System.out.println(YELLOW + "Au revoir !" + RESET);
                    return;
                }
                default -> System.out.println(RED + "Choix invalide." + RESET);
            }
            waitForKey();
        }
    }

    // --- MENUS ---

    private static void printMainMenu() {
        System.out.println("\n" + CYAN + "--- MENU PRINCIPAL ---" + RESET);
        System.out.println("1. 📦 Gérer les Produits (Catalogue)");
        System.out.println("2. 🛒 Mon Panier");
        System.out.println("3. 💳 Mes Commandes");
        System.out.println("9. 👤 Changer d'utilisateur");
        System.out.println("0. 🚪 Quitter");
        System.out.print("Votre choix : ");
    }

    private static void menuProducts() {
        System.out.println("\n" + CYAN + "--- PRODUITS ---" + RESET);
        System.out.println("1. Lister tout");
        System.out.println("2. Détails d'un produit (ID)");
        System.out.println("3. Créer un produit (Admin)");
        System.out.println("4. Supprimer un produit (Admin)");
        System.out.print("Choix : ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> sendRequest("GET", "/products", null);
            case "2" -> {
                System.out.print("ID du produit : ");
                sendRequest("GET", "/products/" + scanner.nextLine(), null);
            }
            case "3" -> createProductWizard();
            case "4" -> {
                System.out.print("ID à supprimer : ");
                sendRequest("DELETE", "/products/" + scanner.nextLine(), null);
            }
        }
    }

    private static void menuCart() {
        System.out.println("\n" + CYAN + "--- PANIER ---" + RESET);
        System.out.println("1. Voir mon panier");
        System.out.println("2. Ajouter un produit");
        System.out.println("3. Retirer un produit");
        System.out.print("Choix : ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> sendRequest("GET", "/carts/" + currentUserId, null);
            case "2" -> addToCartWizard();
            case "3" -> {
                System.out.print("ID du produit à retirer : ");
                sendRequest("DELETE", "/carts/" + currentUserId + "/items/" + scanner.nextLine(), null);
            }
        }
    }

    private static void menuOrders() {
        System.out.println("\n" + CYAN + "--- COMMANDES ---" + RESET);
        System.out.println("1. 💸 Passer commande (Valider panier)");
        System.out.println("2. 📜 Historique des commandes");
        System.out.println("3. 🔍 Voir une commande spécifique");
        System.out.println("4. ✅ Valider une commande (Simu Admin)");
        System.out.print("Choix : ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> sendRequest("POST", "/orders/" + currentUserId, null);
            case "2" -> sendRequest("GET", "/orders", null);
            case "3" -> {
                System.out.print("ID de la commande : ");
                sendRequest("GET", "/orders/" + scanner.nextLine(), null);
            }
            case "4" -> {
                System.out.print("ID de la commande à valider : ");
                sendRequest("PATCH", "/orders/" + scanner.nextLine() + "/validate", null);
            }
        }
    }

    // --- WIZARDS (Aide à la saisie) ---

    private static void createProductWizard() {
        try {
            System.out.print("Nom : ");
            String name = scanner.nextLine();
            System.out.print("Description : ");
            String desc = scanner.nextLine();
            System.out.print("Prix (ex: 10.5) : ");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.print("Stock : ");
            int stock = Integer.parseInt(scanner.nextLine());

            // Construction JSON à la main pour éviter de dupliquer les DTOs ici
            String json = String.format(
                    "{\"name\":\"%s\", \"description\":\"%s\", \"price\":%s, \"stockQuantity\":%d}",
                    name, desc, price, stock
            );
            sendRequest("POST", "/products", json);

        } catch (Exception e) {
            System.out.println(RED + "Erreur de saisie." + RESET);
        }
    }

    private static void addToCartWizard() {
        try {
            System.out.print("ID du Produit : ");
            String prodId = scanner.nextLine();
            System.out.print("Quantité : ");
            int qty = Integer.parseInt(scanner.nextLine());

            String json = String.format(
                    "{\"userId\":\"%s\", \"productId\":\"%s\", \"quantity\":%d}",
                    currentUserId, prodId, qty
            );
            sendRequest("POST", "/carts/add", json);
        } catch (Exception e) {
            System.out.println(RED + "Erreur de saisie." + RESET);
        }
    }

    private static void changeUser() {
        System.out.print("Nouvel UUID : ");
        try {
            currentUserId = UUID.fromString(scanner.nextLine());
            System.out.println(GREEN + "Utilisateur changé !" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "UUID Invalide." + RESET);
        }
    }

    // --- MOTEUR HTTP ---

    private static void sendRequest(String method, String endpoint, String jsonBody) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Content-Type", "application/json");

            switch (method) {
                case "GET" -> builder.GET();
                case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(jsonBody == null ? "" : jsonBody));
                case "PUT" -> builder.PUT(HttpRequest.BodyPublishers.ofString(jsonBody));
                case "DELETE" -> builder.DELETE();
                case "PATCH" -> builder.method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody == null ? "" : jsonBody));
            }

            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());

            System.out.println("------------------------------------------");
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println(GREEN + "SUCCÈS (" + response.statusCode() + ")" + RESET);
                if (!response.body().isEmpty()) {
                    // Pretty Print JSON
                    JsonNode node = mapper.readTree(response.body());
                    System.out.println(node.toPrettyString());
                } else {
                    System.out.println("(Aucun contenu retourné)");
                }
            } else {
                System.out.println(RED + "ERREUR (" + response.statusCode() + ")" + RESET);
                System.out.println(response.body());
            }
            System.out.println("------------------------------------------");

        } catch (IOException | InterruptedException e) {
            System.out.println(RED + "Erreur de connexion : " + e.getMessage() + RESET);
        }
    }

    private static void waitForKey() {
        System.out.println("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }
}