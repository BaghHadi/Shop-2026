package fr.esiea.shop2026;

import fr.esiea.shop2026.domain.entities.Favorite;
import fr.esiea.shop2026.domain.entities.Product;
import fr.esiea.shop2026.domain.entities.User;
import fr.esiea.shop2026.domain.entities.UserEnum;
import fr.esiea.shop2026.domain.repository.FavoriteRepository;
import fr.esiea.shop2026.usecase.service.FavoriteService;
import fr.esiea.shop2026.usecase.service.ProductService;
import fr.esiea.shop2026.usecase.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock private FavoriteRepository favoriteRepository;
    @Mock private ProductService productService;
    @Mock private UserService userService;

    @InjectMocks private FavoriteService favoriteService;

    @Test
    void createFavoriteList_Success() {
        // Arrange
        UUID userId = UUID.randomUUID();

        // ✅ CORRECTION : Instanciation complète de User
        User user = new User(
                userId, "John", "Doe", "test@test.com", "pass", "Address", "0000", UserEnum.CLIENT
        );

        // ✅ CORRECTION : Instanciation complète de Favorite (retour du repo)
        Favorite expectedFav = new Favorite(UUID.randomUUID(), userId, new ArrayList<>());

        when(userService.getUserById(userId)).thenReturn(user);
        when(favoriteRepository.createFavoriteList(user)).thenReturn(expectedFav);

        // Act
        Favorite result = favoriteService.createFavoriteList(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getLinkedUserId());
        verify(userService).getUserById(userId);
        verify(favoriteRepository).createFavoriteList(user);
    }

    @Test
    void addFavoriteToList_Success() {
        // Arrange
        UUID favId = UUID.randomUUID();
        UUID prodId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        // ✅ CORRECTION : Instanciation complète de Favorite
        Favorite fav = new Favorite(favId, userId, new ArrayList<>());

        // ✅ CORRECTION : Instanciation complète de Product (C'était ton erreur principale)
        Product prod = new Product(
                prodId, "Test Product", "Description", BigDecimal.TEN, 10
        );

        when(favoriteRepository.getFavoriteListById(favId)).thenReturn(fav);
        when(productService.getProduct(prodId)).thenReturn(prod);

        // On dit au mock : "Si on t'appelle avec ce produit et cette liste, retourne la liste"
        when(favoriteRepository.addFavoriteToList(prod, fav)).thenReturn(fav);

        // Act
        Favorite result = favoriteService.addFavoriteToList(favId, prodId);

        // Assert
        assertNotNull(result);
        verify(favoriteRepository).addFavoriteToList(prod, fav);
    }

    @Test
    void removeFavoriteFromList_Success() {
        // Arrange
        UUID favId = UUID.randomUUID();
        UUID prodId = UUID.randomUUID();

        Favorite fav = new Favorite(favId, UUID.randomUUID(), new ArrayList<>());
        Product prod = new Product(prodId, "P", "D", BigDecimal.ONE, 5);

        when(favoriteRepository.getFavoriteListById(favId)).thenReturn(fav);
        when(productService.getProduct(prodId)).thenReturn(prod);
        when(favoriteRepository.removeFavoriteFromList(prod, fav)).thenReturn(fav);

        // Act
        favoriteService.removeFavoriteFromList(favId, prodId);

        // Assert
        verify(favoriteRepository).removeFavoriteFromList(prod, fav);
    }
}