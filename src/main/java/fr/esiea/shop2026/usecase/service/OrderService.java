package fr.esiea.shop2026.usecase.service;

import fr.esiea.shop2026.domain.entities.Cart;
import fr.esiea.shop2026.domain.entities.Order;
import fr.esiea.shop2026.domain.repository.CartRepository;
import fr.esiea.shop2026.domain.repository.OrderEventRepository;
import fr.esiea.shop2026.domain.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderEventRepository orderEventRepository; // Interface vers Kafka

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        OrderEventRepository orderEventRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.orderEventRepository = orderEventRepository;
    }

    public Order createOrderFromCart(UUID userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cannot create order: Cart is empty or not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot create order: Cart is empty");
        }

        Order order = new Order(
                UUID.randomUUID(),
                userId,
                cart.getItems(),
                cart.calculateTotal()
        );
        Order savedOrder = orderRepository.save(order);

        cartRepository.deleteById(cart.getId());
        orderEventRepository.publishOrderCreated(savedOrder);

        return savedOrder;
    }
    public Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    public Order validateOrder(UUID orderId) {
        Order order = getOrder(orderId); // Récupère la commande
        order.setStatus("CONFIRMED");    // Change le statut
        return orderRepository.save(order); // Sauvegarde
    }
}