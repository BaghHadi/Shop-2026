package fr.esiea.shop2026.adapters.rest;

import fr.esiea.shop2026.domain.entities.Cart;
import fr.esiea.shop2026.usecase.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // GET /carts/{userId} (Voir le panier d'un client)
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable UUID userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    // POST /carts/add (Ajouter un produit au panier)
    // On utilise des RequestParam pour simplifier : /carts/add?userId=...&productId=...&quantity=1
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestParam UUID userId,
                                          @RequestParam UUID productId,
                                          @RequestParam int quantity) {
        try {
            Cart cart = cartService.addProductToCart(userId, productId, quantity);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build(); // Erreur de stock
        }
    }

    // DELETE /carts/{userId} (Vider le panier)
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable UUID userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
    // DELETE /carts/{userId}/items/{productId}
    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<Cart> removeProduct(@PathVariable UUID userId, @PathVariable UUID productId) {
        try {
            Cart cart = cartService.removeProductFromCart(userId, productId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}