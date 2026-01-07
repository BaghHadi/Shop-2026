package fr.esiea.shop2026.adapters.infrastructure.repository;

import fr.esiea.shop2026.adapters.infrastructure.entity.ProductEntity;
import fr.esiea.shop2026.adapters.infrastructure.mapper.ProductMapper;
import fr.esiea.shop2026.domain.entities.Product;
import fr.esiea.shop2026.domain.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component // Important pour que Spring le trouve
public class ProductRepositoryAdapter implements ProductRepository {

    private final SpringDataProductRepository jpaRepository;

    public ProductRepositoryAdapter(SpringDataProductRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Product save(Product product) {
        // 1. UTILISATION DU MAPPER : Domaine -> Entity JPA
        ProductEntity entity = ProductMapper.toEntity(product);

        // 2. Sauvegarde en BDD
        ProductEntity savedEntity = jpaRepository.save(entity);

        // 3. UTILISATION DU MAPPER : Entity JPA -> Domaine
        return ProductMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        // Le mapper convertit le résultat trouvé
        return jpaRepository.findById(id).map(ProductMapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream()
                .map(ProductMapper::toDomain) // Conversion de toute la liste
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}