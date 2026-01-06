package fr.esiea.shop2026;

import fr.esiea.shop2026.domain.entities.Cart;
import fr.esiea.shop2026.domain.entities.Order;
import fr.esiea.shop2026.domain.entities.Product;
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

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock // Simule le Repository (car on n'a pas encore la vraie BDD)
    CartRepository cartRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderEventRepository orderEventRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    void shouldCreateOrderSuccessfully() {
        // 1. PREPARER LES DONNEES (Arrange)
        UUID userId = UUID.randomUUID();
        Product tShirt = new Product(UUID.randomUUID(), "T-Shirt", "Coton", new BigDecimal("20.00"), 10);

        Cart mockCart = new Cart(UUID.randomUUID(), userId);
        mockCart.addItem(tShirt, 2); // Total = 40.00

        // On dit au mock : "Si on te demande le panier de cet ID, renvoie mockCart"
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(mockCart));

        // On dit au mock : "Si on te demande de sauvegarder, renvoie une commande avec un ID"
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(UUID.randomUUID()); // On simule l'ID généré par la BDD
            return o;
        });

        // 2. EXECUTER L'ACTION (Act)
        Order createdOrder = orderService.createOrderFromCart(userId);

        // 3. VERIFIER LE RESULTAT (Assert)
        // Vérifie que la commande a le bon montant (20 * 2 = 40)
        assert createdOrder.getTotalAmount().compareTo(new BigDecimal("40.00")) == 0;

        // Vérifie que le panier a bien été supprimé
        verify(cartRepository).deleteById(mockCart.getId());

        // Vérifie que l'événement Kafka a bien été envoyé
        verify(orderEventRepository).publishOrderCreated(createdOrder);

        System.out.println("✅ Test réussi : La commande a été créée et l'événement envoyé !");
    }
}