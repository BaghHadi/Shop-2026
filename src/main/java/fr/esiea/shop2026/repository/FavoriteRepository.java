package fr.esiea.shop2026.repository;

import fr.esiea.shop2026.domain.entities.Favorite;
import fr.esiea.shop2026.domain.entities.Product;
import fr.esiea.shop2026.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface FavoriteRepository {

    // Create a Favorite List
    Favorite createFavoriteList(User linkedUser);

    // Remove a User Favorite List
    boolean removeFavoriteList(Favorite user);


    // Add a Product to a Favorite list
    Favorite addFavoriteToList(Product product, Favorite favoriteList);

    // Remove a Product from a Favorite list
    Favorite removeFavoriteFromList(Product product, Favorite favoriteList);


    // Get all favorites of a user from DB
    List<Favorite> getFavoriteListByUser(User user);

    // Get a Favorite list by its ID
    Favorite getFavoriteListById(UUID favoriteListID);

    // Check if a product is in a favorite list
    boolean checkIfProductInList(Product product, Favorite favoriteList);
}

