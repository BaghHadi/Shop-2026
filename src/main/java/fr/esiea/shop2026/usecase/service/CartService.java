package fr.esiea.shop2026.usecase.service;

import fr.esiea.shop2026.domain.entities.Cart;
import fr.esiea.shop2026.domain.entities.Product;
import fr.esiea.shop2026.domain.exception.InsufficientStockException;
import fr.esiea.shop2026.domain.exception.NotFoundException;
import fr.esiea.shop2026.domain.repository.CartRepository;
import fr.esiea.shop2026.domain.repository.ProductRepository;

import java.util.UUID;

public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public Cart addProductToCart(UUID userId, UUID productId, int quantity) {
        // On récupère ou on crée le panier
        Cart cart = cartRepository.findByUserId(userId)
                .orElse(new Cart(UUID.randomUUID(), userId));

        // ✅ Utilisation de NotFoundException si le produit n'existe pas
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product", productId));

        // ✅ Utilisation de InsufficientStockException
        if (!product.hasStock(quantity)) {
            throw new InsufficientStockException("Not enough stock for product: " + product.getName());
        }

        cart.addItem(product, quantity);
        return cartRepository.save(cart);
    }

    public Cart getCart(UUID userId) {
        // Stratégie : si pas de panier, on en renvoie un vide (Lazy loading)
        return cartRepository.findByUserId(userId)
                .orElse(new Cart(UUID.randomUUID(), userId));
    }

    public void clearCart(UUID userId) {
        Cart cart = getCart(userId);
        cartRepository.deleteById(cart.getId());
    }

    public Cart removeProductFromCart(UUID userId, UUID productId) {
        Cart cart = getCart(userId);
        cart.removeProduct(productId);
        return cartRepository.save(cart);
    }
}