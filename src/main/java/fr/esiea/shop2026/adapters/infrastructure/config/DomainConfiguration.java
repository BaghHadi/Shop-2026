package fr.esiea.shop2026.adapters.infrastructure.config;

import fr.esiea.shop2026.domain.repository.CartRepository;
import fr.esiea.shop2026.domain.repository.OrderEventRepository;
import fr.esiea.shop2026.domain.repository.OrderRepository;
import fr.esiea.shop2026.domain.repository.ProductRepository;
import fr.esiea.shop2026.usecase.service.CartService;
import fr.esiea.shop2026.usecase.service.OrderService;
import fr.esiea.shop2026.usecase.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

    @Bean
    public ProductService productService(ProductRepository productRepository) {
        return new ProductService(productRepository);
    }

    @Bean
    public CartService cartService(CartRepository cartRepository, ProductRepository productRepository) {
        return new CartService(cartRepository, productRepository);
    }

    @Bean
    public OrderService orderService(OrderRepository orderRepository,
                                     CartRepository cartRepository,
                                     OrderEventRepository orderEventRepository) {
        return new OrderService(orderRepository, cartRepository, orderEventRepository);
    }
}