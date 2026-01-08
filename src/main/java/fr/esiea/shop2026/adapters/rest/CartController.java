package fr.esiea.shop2026.adapters.rest;

import fr.esiea.shop2026.domain.entities.Cart;
import fr.esiea.shop2026.usecase.dto.AddToCartDTO;
import fr.esiea.shop2026.usecase.dto.CartItemDTO;
import fr.esiea.shop2026.usecase.dto.CartResponseDTO;
import fr.esiea.shop2026.usecase.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/carts")
@Tag(name = "Gestion Paniers", description = "Opérations pour gérer le panier des utilisateurs (Ajout, suppression, consultation).")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // POST /carts/add (Ajouter un item via DTO)
    @Operation(summary = "Ajouter au panier", description = "Ajoute un produit au panier d'un utilisateur. Crée le panier s'il n'existe pas.")
    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addItem(@RequestBody AddToCartDTO dto) {
        try {
            Cart cart = cartService.addProductToCart(dto.userId, dto.productId, dto.quantity);
            return ResponseEntity.ok(mapToDTO(cart));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build(); // Produit non trouvé ou stock insuffisant
        }
    }

    // GET /carts/{userId} (Voir le panier)
    @Operation(summary = "Voir le panier", description = "Récupère le contenu actuel du panier d'un utilisateur.")
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable UUID userId) {
        try {
            Cart cart = cartService.getCart(userId);
            return ResponseEntity.ok(mapToDTO(cart));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /carts/{userId}/items/{productId} (Retirer un item)
    @Operation(summary = "Retirer un article", description = "Supprime un produit spécifique du panier.")
    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartResponseDTO> removeProduct(@PathVariable UUID userId, @PathVariable UUID productId) {
        try {
            Cart cart = cartService.removeProductFromCart(userId, productId);
            return ResponseEntity.ok(mapToDTO(cart));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Helper de conversion Entity -> DTO ---
    private CartResponseDTO mapToDTO(Cart cart) {
        // 1. Convertir les items
        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(item -> new CartItemDTO(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getProduct().getPrice(),
                        item.getSubTotal()
                )).collect(Collectors.toList());

        // 2. Calculer le total (si pas déjà fait dans l'entité)
        BigDecimal total = itemDTOs.stream()
                .map(i -> i.totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Retourner le DTO Panier
        return new CartResponseDTO(
                cart.getId(),
                cart.getUserId(),
                itemDTOs,
                total
        );
    }
}