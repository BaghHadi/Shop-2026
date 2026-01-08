package fr.esiea.shop2026.usecase.service;

import fr.esiea.shop2026.domain.entities.Cart;
import fr.esiea.shop2026.domain.entities.Product;
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
        Cart cart = cartRepository.findByUserId(userId)
                .orElse(new Cart(UUID.randomUUID(), userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.hasStock(quantity)) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }

        cart.addItem(product, quantity);

        return cartRepository.save(cart);
    }

    public Cart getCart(UUID userId) {
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