package fr.esiea.shop2026.adapters.rest;

import fr.esiea.shop2026.domain.entities.Order;
import fr.esiea.shop2026.usecase.dto.OrderItemDTO;
import fr.esiea.shop2026.usecase.dto.OrderResponseDTO;
import fr.esiea.shop2026.usecase.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@Tag(name = "Gestion Commandes", description = "Opérations de création, validation et consultation des commandes.")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // POST /orders/{userId}
    @Operation(summary = "Créer une commande", description = "Transforme le panier actuel de l'utilisateur en une nouvelle commande en statut PENDING.")
    @PostMapping("/{userId}")
    public ResponseEntity<OrderResponseDTO> createOrder(@PathVariable UUID userId) {
        try {
            Order order = orderService.createOrderFromCart(userId);
            return ResponseEntity.ok(mapToDTO(order));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build(); // Panier vide
        }
    }

    // GET /orders/{id}
    @Operation(summary = "Voir une commande", description = "Récupère les détails d'une commande spécifique (statut, total, articles).")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(mapToDTO(orderService.getOrder(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /orders
    @Operation(summary = "Lister toutes les commandes", description = "Récupère l'historique complet de toutes les commandes passées sur le site.")
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> dtos = orderService.getAllOrders().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // PATCH /orders/{id}/validate
    @Operation(summary = "Valider une commande", description = "Passe le statut d'une commande de PENDING à CONFIRMED.")
    @PatchMapping("/{id}/validate")
    public ResponseEntity<OrderResponseDTO> validateOrder(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(mapToDTO(orderService.validateOrder(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Helper de conversion Entity -> DTO ---
    private OrderResponseDTO mapToDTO(Order order) {
        // 1. On convertit d'abord les items (Entité -> DTO)
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> new OrderItemDTO(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getProduct().getPrice(),
                        item.getSubTotal()
                )).collect(Collectors.toList());

        // 2. On crée le DTO global
        return new OrderResponseDTO(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                itemDTOs
        );
    }
}