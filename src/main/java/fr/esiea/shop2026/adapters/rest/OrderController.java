package fr.esiea.shop2026.adapters.rest;

import fr.esiea.shop2026.domain.entities.Order;
import fr.esiea.shop2026.usecase.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // POST /orders/{userId} (Transformer le panier en commande)
    @PostMapping("/{userId}")
    public ResponseEntity<Order> createOrder(@PathVariable UUID userId) {
        try {
            Order order = orderService.createOrderFromCart(userId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build(); // Panier vide
        }
    }

    // GET /orders/{id} (Voir une commande)
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(orderService.getOrder(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    // PATCH /orders/{id}/validate
    @PatchMapping("/{id}/validate")
    public ResponseEntity<Order> validateOrder(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(orderService.validateOrder(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}