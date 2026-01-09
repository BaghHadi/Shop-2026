package fr.esiea.shop2026;

import fr.esiea.shop2026.domain.entities.Cart;
import fr.esiea.shop2026.domain.entities.Order;
import fr.esiea.shop2026.domain.entities.Product;
import fr.esiea.shop2026.domain.exception.EmptyCartException;
import fr.esiea.shop2026.domain.repository.CartRepository;
import fr.esiea.shop2026.domain.repository.OrderEventRepository;
import fr.esiea.shop2026.domain.repository.OrderRepository;
import fr.esiea.shop2026.usecase.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private CartRepository cartRepository;
    @Mock private OrderEventRepository orderEventRepository;

    @InjectMocks private OrderService orderService;

    @Test
    void createOrderFromCart_Success() {
        // Arrange
        UUID userId = UUID.randomUUID();
        // ✅ CORRECTION
        Cart cart = new Cart(UUID.randomUUID(), userId);

        Product p = new Product(UUID.randomUUID(), "P1", "D", BigDecimal.TEN, 10);
        cart.addItem(p, 2);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Order order = orderService.createOrderFromCart(userId);

        // Assert
        assertNotNull(order);
        assertEquals(userId, order.getUserId());
        verify(cartRepository).deleteById(cart.getId());
        verify(orderEventRepository).publishOrderCreated(any(Order.class));
    }

    @Test
    void createOrderFromCart_EmptyCart_ThrowsException() {
        // Arrange
        UUID userId = UUID.randomUUID();
        // ✅ CORRECTION : Panier vide mais bien instancié
        Cart emptyCart = new Cart(UUID.randomUUID(), userId);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(emptyCart));

        // Act & Assert
        assertThrows(EmptyCartException.class, () -> orderService.createOrderFromCart(userId));
        verify(orderRepository, never()).save(any());
    }

}