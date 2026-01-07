package fr.esiea.shop2026.adapters.infrastructure.mapper;

import fr.esiea.shop2026.adapters.infrastructure.entity.ProductEntity;
import fr.esiea.shop2026.domain.entities.Product;

public class ProductMapper {

    // Entity (JPA) -> Domain (Métier)
    public static Product toDomain(ProductEntity entity) {
        if (entity == null) return null;

        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStockQuantity()
        );
    }

    // Domain (Métier) -> Entity (JPA)
    public static ProductEntity toEntity(Product domain) {
        if (domain == null) return null;

        return new ProductEntity(
                domain.getId(),
                domain.getName(),
                domain.getDescription(),
                domain.getPrice(),
                domain.getStockQuantity()
        );
    }
}