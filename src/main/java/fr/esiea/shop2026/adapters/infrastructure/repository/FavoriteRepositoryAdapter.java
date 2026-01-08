package fr.esiea.shop2026.adapters.infrastructure.repository;

import fr.esiea.shop2026.adapters.infrastructure.entity.FavoriteEntity;
import fr.esiea.shop2026.adapters.infrastructure.entity.ProductEntity;
import fr.esiea.shop2026.domain.entities.Favorite;
import fr.esiea.shop2026.domain.entities.Product;
import fr.esiea.shop2026.domain.entities.User;
import fr.esiea.shop2026.domain.exception.NotFoundException;
import fr.esiea.shop2026.domain.repository.FavoriteRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class FavoriteRepositoryAdapter implements FavoriteRepository {

    private final SpringDataFavoriteRepository favoriteJpaRepository;
    private final SpringDataProductRepository productJpaRepository;

    public FavoriteRepositoryAdapter(SpringDataFavoriteRepository favoriteJpaRepository,
                                     SpringDataProductRepository productJpaRepository) {
        this.favoriteJpaRepository = favoriteJpaRepository;
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Favorite createFavoriteList(User linkedUser) {
        FavoriteEntity entity = new FavoriteEntity();
        entity.setId(UUID.randomUUID());
        entity.setLinkedUserId(linkedUser.getId());
        entity.setProducts(new ArrayList<>());

        FavoriteEntity saved = favoriteJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public boolean removeFavoriteList(Favorite favoriteList) {
        if (favoriteList != null && favoriteList.getId() != null && favoriteJpaRepository.existsById(favoriteList.getId())) {
            favoriteJpaRepository.deleteById(favoriteList.getId());
            return true;
        }
        return false;
    }

    @Override
    public Favorite addFavoriteToList(Product product, Favorite favoriteList) {
        // 1. On récupère la liste de favoris (Entité JPA)
        FavoriteEntity favEntity = favoriteJpaRepository.findById(favoriteList.getId())
                .orElseThrow(() -> new NotFoundException("FavoriteList", favoriteList.getId()));

        // 2. On récupère le produit (Entité JPA) pour être sûr qu'il est géré par Hibernate
        ProductEntity prodEntity = productJpaRepository.findById(product.getId())
                .orElseThrow(() -> new NotFoundException("Product", product.getId()));

        // 3. On ajoute si pas déjà présent
        boolean alreadyExists = favEntity.getProducts().stream()
                .anyMatch(p -> p.getId().equals(prodEntity.getId()));

        if (!alreadyExists) {
            favEntity.getProducts().add(prodEntity);
            favEntity = favoriteJpaRepository.save(favEntity);
        }

        return toDomain(favEntity);
    }

    @Override
    public Favorite removeFavoriteFromList(Product product, Favorite favoriteList) {
        FavoriteEntity favEntity = favoriteJpaRepository.findById(favoriteList.getId())
                .orElseThrow(() -> new NotFoundException("FavoriteList", favoriteList.getId()));

        // On retire le produit de la liste
        favEntity.getProducts().removeIf(p -> p.getId().equals(product.getId()));

        FavoriteEntity saved = favoriteJpaRepository.save(favEntity);
        return toDomain(saved);
    }

    @Override
    public List<Favorite> getFavoriteListByUser(User user) {
        // Supposons qu'un user a une seule liste, mais l'interface demande une List<Favorite>
        return favoriteJpaRepository.findByLinkedUserId(user.getId())
                .map(this::toDomain)
                .map(Collections::singletonList) // On met l'objet unique dans une liste
                .orElse(new ArrayList<>());
    }

    @Override
    public Favorite getFavoriteListById(UUID favoriteListID) {
        return favoriteJpaRepository.findById(favoriteListID)
                .map(this::toDomain)
                .orElseThrow(() -> new NotFoundException("FavoriteList", favoriteListID));
    }

    @Override
    public boolean checkIfProductInList(Product product, Favorite favoriteList) {
        FavoriteEntity favEntity = favoriteJpaRepository.findById(favoriteList.getId())
                .orElseThrow(() -> new NotFoundException("FavoriteList", favoriteList.getId()));

        return favEntity.getProducts().stream()
                .anyMatch(p -> p.getId().equals(product.getId()));
    }

    // --- Mapper Privé (Entity -> Domain) ---
    private Favorite toDomain(FavoriteEntity e) {
        List<Product> domainProducts = e.getProducts().stream()
                .map(pe -> new Product(pe.getId(), pe.getName(), pe.getDescription(), pe.getPrice(), pe.getStockQuantity()))
                .collect(Collectors.toList());

        return new Favorite(e.getId(), e.getLinkedUserId(), domainProducts);
    }
}