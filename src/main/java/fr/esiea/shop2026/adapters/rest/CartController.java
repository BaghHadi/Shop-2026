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

    // POST /carts/add
    @Operation(summary = "Ajouter au panier", description = "Ajoute un produit au panier d'un utilisateur. Crée le panier s'il n'existe pas.")
    @PostMapping
    public ResponseEntity<CartResponseDTO> addItem(@RequestBody AddToCartDTO dto) {
        Cart cart = cartService.addProductToCart(dto.userId, dto.productId, dto.quantity);
        return ResponseEntity.ok(mapToDTO(cart));
    }

    // GET /carts/{userId}
    @Operation(summary = "Voir le panier", description = "Récupère le contenu actuel du panier d'un utilisateur.")
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable UUID userId) {
        Cart cart = cartService.getCart(userId);
        return ResponseEntity.ok(mapToDTO(cart));
    }

    // DELETE /carts/{userId}/items/{productId}
    @Operation(summary = "Retirer un article", description = "Supprime un produit spécifique du panier.")
    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartResponseDTO> removeProduct(@PathVariable UUID userId, @PathVariable UUID productId) {
        // ✅ Pas de try-catch
        Cart cart = cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.ok(mapToDTO(cart));
    }

    private CartResponseDTO mapToDTO(Cart cart) {
        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(item -> new CartItemDTO(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getProduct().getPrice(),
                        item.getSubTotal()
                )).collect(Collectors.toList());

        BigDecimal total = itemDTOs.stream()
                .map(i -> i.totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDTO(
                cart.getId(),
                cart.getUserId(),
                itemDTOs,
                total
        );
    }
}