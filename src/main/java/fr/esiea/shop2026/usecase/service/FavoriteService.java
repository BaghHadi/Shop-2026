package fr.esiea.shop2026.usecase.service;

import fr.esiea.shop2026.domain.entities.Favorite;
import fr.esiea.shop2026.domain.entities.Product;
import fr.esiea.shop2026.domain.entities.User;
import fr.esiea.shop2026.domain.repository.FavoriteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductService productService;
    private final UserService userService;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           ProductService productService,
                           UserService userService) {
        this.favoriteRepository = favoriteRepository;
        this.productService = productService;
        this.userService = userService;
    }

    public Favorite createFavoriteList(UUID userId) {
        // 1. On récupère l'objet User complet
        User user = userService.getUserById(userId);

        // 2. On appelle le repo
        return favoriteRepository.createFavoriteList(user);
    }

    public boolean removeFavoriteListById(UUID favoriteListId) {
        // 1. On récupère la liste complète
        Favorite favoriteList = getFavoriteListById(favoriteListId);

        // 2. On supprime
        return favoriteRepository.removeFavoriteList(favoriteList);
    }

    public Favorite addFavoriteToList(UUID favoriteListId, UUID productId) {
        // 1. Récupération des objets complets (Favorite + Product)
        Favorite favoriteList = getFavoriteListById(favoriteListId);
        Product product = productService.getProduct(productId);

        // 2. Appel du repo avec les objets
        return favoriteRepository.addFavoriteToList(product, favoriteList);
    }

    public Favorite removeFavoriteFromList(UUID favoriteListId, UUID productId) {
        // 1. Récupération des objets complets
        Favorite favoriteList = getFavoriteListById(favoriteListId);
        Product product = productService.getProduct(productId);

        // 2. Appel du repo
        return favoriteRepository.removeFavoriteFromList(product, favoriteList);
    }

    public List<Favorite> getFavoriteListByUser(UUID userId) {
        User user = userService.getUserById(userId);
        return favoriteRepository.getFavoriteListByUser(user);
    }

    public Favorite getFavoriteListById(UUID favoriteListID) {
        // Le repo gère déjà l'exception normalement via l'Adapter, sinon on pourrait ajouter un try/catch
        return favoriteRepository.getFavoriteListById(favoriteListID);
    }

    public boolean checkIfProductInList(UUID favoriteListId, UUID productId) {
        Favorite favoriteList = getFavoriteListById(favoriteListId);
        Product product = productService.getProduct(productId);

        return favoriteRepository.checkIfProductInList(product, favoriteList);
    }
}