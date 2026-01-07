package fr.esiea.shop2026.adapters.infrastructure.repository;

import fr.esiea.shop2026.adapters.infrastructure.entity.OrderEntity;
import fr.esiea.shop2026.adapters.infrastructure.entity.OrderItemEntity;
import fr.esiea.shop2026.adapters.infrastructure.mapper.OrderMapper;
import fr.esiea.shop2026.domain.entities.CartItem;
import fr.esiea.shop2026.domain.entities.Order;
import fr.esiea.shop2026.domain.entities.Product;
import fr.esiea.shop2026.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final SpringDataOrderRepository orderJpaRepository;
    private final SpringDataProductRepository productJpaRepository;

    @Override
    public Order save(Order order) {
        // UTILISATION DU MAPPER ICI
        OrderEntity entity = OrderMapper.toEntity(order);

        orderJpaRepository.save(entity);
        return order;
    }

    @Override
    public Optional<Order> findById(UUID id) {
        Optional<OrderEntity> entityOpt = orderJpaRepository.findById(id);
        if (entityOpt.isEmpty()) return Optional.empty();

        OrderEntity entity = entityOpt.get();
        List<CartItem> domainItems = new ArrayList<>();

        // Logique de reconstruction manuelle
        for (OrderItemEntity itemEntity : entity.getItems()) {
            productJpaRepository.findById(itemEntity.getProductId()).ifPresent(pe -> {
                Product p = new Product(pe.getId(), pe.getName(), pe.getDescription(), itemEntity.getPriceAtOrderTime(), pe.getStockQuantity());
                domainItems.add(new CartItem(p, itemEntity.getQuantity()));
            });
        }

        Order order = new Order(entity.getId(), entity.getUserId(), domainItems, entity.getTotalAmount());
        order.setStatus(entity.getStatus());
        return Optional.of(order);
    }
    @Override
    public List<Order> findAll() {
        return orderJpaRepository.findAll().stream()
                .map(this::toDomain) // On convertit chaque ligne trouvée
                .collect(Collectors.toList());
    }

    private Order toDomain(OrderEntity entity) {
        List<CartItem> domainItems = new ArrayList<>();

        for (OrderItemEntity itemEntity : entity.getItems()) {
            productJpaRepository.findById(itemEntity.getProductId()).ifPresent(pe -> {
                Product p = new Product(pe.getId(), pe.getName(), pe.getDescription(), itemEntity.getPriceAtOrderTime(), pe.getStockQuantity());
                domainItems.add(new CartItem(p, itemEntity.getQuantity()));
            });
        }

        Order order = new Order(entity.getId(), entity.getUserId(), domainItems, entity.getTotalAmount());
        order.setStatus(entity.getStatus());
        return order;
    }
}