package fr.esiea.shop2026.adapters.infrastructure.mapper;

import fr.esiea.shop2026.adapters.infrastructure.entity.OrderEntity;
import fr.esiea.shop2026.adapters.infrastructure.entity.OrderItemEntity;
import fr.esiea.shop2026.domain.entities.CartItem;
import fr.esiea.shop2026.domain.entities.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderEntity toEntity(Order domain) {
        if (domain == null) return null;

        OrderEntity entity = new OrderEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setTotalAmount(domain.getTotalAmount());
        entity.setStatus(domain.getStatus());
        entity.setCreatedAt(domain.getCreatedAt());

        if (domain.getItems() != null) {
            List<OrderItemEntity> itemEntities = domain.getItems().stream()
                    .map(OrderMapper::toItemEntity)
                    .collect(Collectors.toList());
            entity.setItems(itemEntities);
        }

        return entity;
    }

    private static OrderItemEntity toItemEntity(CartItem domainItem) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setProductId(domainItem.getProduct().getId());
        entity.setQuantity(domainItem.getQuantity());
        entity.setPriceAtOrderTime(domainItem.getProduct().getPrice());
        return entity;
    }
}