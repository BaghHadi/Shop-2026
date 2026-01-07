package fr.esiea.shop2026.adapters.infrastructure.repository;

import fr.esiea.shop2026.adapters.infrastructure.entity.CartEntity;
import fr.esiea.shop2026.adapters.infrastructure.entity.CartItemEntity;
import fr.esiea.shop2026.adapters.infrastructure.entity.ProductEntity;
import fr.esiea.shop2026.adapters.infrastructure.mapper.CartMapper;
import fr.esiea.shop2026.domain.entities.Cart;
import fr.esiea.shop2026.domain.entities.Product;
import fr.esiea.shop2026.domain.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CartRepositoryAdapter implements CartRepository {

    private final SpringDataCartRepository cartJpaRepository;
    private final SpringDataProductRepository productJpaRepository;

    @Override
    public Cart save(Cart cart) {
        // j'use lemapper ici
        CartEntity entity = CartMapper.toEntity(cart);

        cartJpaRepository.save(entity);
        return cart;
    }

    @Override
    public Optional<Cart> findByUserId(UUID userId) {
        Optional<CartEntity> entityOpt = cartJpaRepository.findByUserId(userId);
        if (entityOpt.isEmpty()) return Optional.empty();

        CartEntity entity = entityOpt.get();
        Cart cart = new Cart(entity.getId(), entity.getUserId());

        // Logique complexe pour recharger les produits (on ne peut pas utiliser Mapper statique ici)
        for (CartItemEntity itemEntity : entity.getItems()) {
            Optional<ProductEntity> productEntity = productJpaRepository.findById(itemEntity.getProductId());
            if (productEntity.isPresent()) {
                ProductEntity pe = productEntity.get();
                Product product = new Product(pe.getId(), pe.getName(), pe.getDescription(), pe.getPrice(), pe.getStockQuantity());
                cart.addItem(product, itemEntity.getQuantity());
            }
        }
        return Optional.of(cart);
    }

    @Override
    public void deleteById(UUID id) {
        cartJpaRepository.deleteById(id);
    }
}