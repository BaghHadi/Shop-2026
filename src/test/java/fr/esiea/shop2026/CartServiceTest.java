package fr.esiea.shop2026;

import fr.esiea.shop2026.domain.entities.Cart;
import fr.esiea.shop2026.domain.entities.Product;
import fr.esiea.shop2026.domain.exception.InsufficientStockException;
import fr.esiea.shop2026.domain.exception.NotFoundException;
import fr.esiea.shop2026.domain.repository.CartRepository;
import fr.esiea.shop2026.domain.repository.ProductRepository;
import fr.esiea.shop2026.usecase.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void addProductToCart_Success() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        // ✅ CORRECTION : On passe des UUID au constructeur
        Cart cart = new Cart(UUID.randomUUID(), userId);
        Product product = new Product(productId, "Item", "Desc", BigDecimal.TEN, 100);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Act
        cartService.addProductToCart(userId, productId, 2);

        // Assert
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());
        verify(cartRepository).save(cart);
    }

    @Test
    void addProductToCart_InsufficientStock_ThrowsException() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        // ✅ CORRECTION
        Cart cart = new Cart(UUID.randomUUID(), userId);
        Product product = new Product(productId, "Item", "Desc", BigDecimal.TEN, 1); // Stock = 1

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act & Assert
        assertThrows(InsufficientStockException.class, () ->
                cartService.addProductToCart(userId, productId, 5) // On demande 5
        );
        verify(cartRepository, never()).save(any());
    }

    @Test
    void addProductToCart_ProductNotFound_ThrowsException() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // ✅ CORRECTION ICI (C'était là ton erreur principale)
        // Au lieu de new Cart(), on met new Cart(UUID, UUID)
        when(cartRepository.findByUserId(userId))
                .thenReturn(Optional.of(new Cart(UUID.randomUUID(), userId)));

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cartService.addProductToCart(userId, productId, 1));
    }
}