package fr.esiea.shop2026.usecase.service;

import fr.esiea.shop2026.domain.entities.Cart;
import fr.esiea.shop2026.domain.entities.Order;
import fr.esiea.shop2026.domain.exception.EmptyCartException;
import fr.esiea.shop2026.domain.exception.NotFoundException;
import fr.esiea.shop2026.domain.repository.CartRepository;
import fr.esiea.shop2026.domain.repository.OrderEventRepository;
import fr.esiea.shop2026.domain.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderEventRepository orderEventRepository;

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        OrderEventRepository orderEventRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.orderEventRepository = orderEventRepository;
    }

    public Order createOrderFromCart(UUID userId) {
        // ✅ NotFoundException si le panier n'existe pas en base
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart", userId));

        // ✅ EmptyCartException si le panier est vide
        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException("Cannot create order from an empty cart.");
        }

        Order order = new Order(
                UUID.randomUUID(),
                userId,
                cart.getItems(),
                cart.calculateTotal()
        );
        Order savedOrder = orderRepository.save(order);

        // Une fois commandé, on vide le panier et on notifie
        cartRepository.deleteById(cart.getId());
        orderEventRepository.publishOrderCreated(savedOrder);

        return savedOrder;
    }

    public Order getOrder(UUID orderId) {
        // ✅ NotFoundException
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order", orderId));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order validateOrder(UUID orderId) {
        Order order = getOrder(orderId); // utilise déjà NotFoundException ci-dessus
        order.setStatus("CONFIRMED");
        return orderRepository.save(order);
    }
}