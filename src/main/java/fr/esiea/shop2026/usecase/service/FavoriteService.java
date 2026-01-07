package fr.esiea.shop2026.usecase.service;

import fr.esiea.shop2026.domain.entities.Favorite;
import fr.esiea.shop2026.domain.entities.Product;
import fr.esiea.shop2026.domain.entities.User;
import fr.esiea.shop2026.repository.FavoriteRepository;

import java.util.List;
import java.util.UUID;

public class FavoriteService {

    public FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }


    public Favorite createFavoriteList(User linkedUser) {
        return null;
    }


    public boolean removeFavoriteList(Favorite user) {
        return false;
    }


    public Favorite addFavoriteToList(Product product, Favorite favoriteList) {
        return null;
    }


    public Favorite removeFavoriteFromList(Product product, Favorite favoriteList) {
        return null;
    }


    public List<Favorite> getFavoriteListByUser(User user) {
        return List.of();
    }


    public Favorite getFavoriteListById(UUID favoriteListID) {
        return null;
    }


    public boolean checkIfProductInList(Product product, Favorite favoriteList) {
        return false;
    }
}
