package fr.esiea.shop2026.adapters.rest;

import fr.esiea.shop2026.domain.entities.Favorite;
import fr.esiea.shop2026.usecase.dto.FavoriteResponseDTO;
import fr.esiea.shop2026.usecase.dto.ProductSummaryDTO;
import fr.esiea.shop2026.usecase.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favorites")
@Tag(name = "Gestion Favoris", description = "Gestion des listes de souhaits (Wishlist).")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @Operation(summary = "Créer une liste", description = "Crée une liste de favoris pour un utilisateur.")
    @PostMapping("/user/{userId}")
    public ResponseEntity<FavoriteResponseDTO> createList(@PathVariable UUID userId) {
        Favorite fav = favoriteService.createFavoriteList(userId);
        return ResponseEntity.ok(mapToDTO(fav));
    }

    @Operation(summary = "Voir mes favoris", description = "Récupère les favoris d'un utilisateur.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavoriteResponseDTO>> getByUserId(@PathVariable UUID userId) {
        List<Favorite> favorites = favoriteService.getFavoriteListByUser(userId);
        List<FavoriteResponseDTO> dtos = favorites.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Ajouter un produit", description = "Ajoute un produit dans une liste de favoris existante.")
    @PostMapping("/{favoriteId}/products/{productId}")
    public ResponseEntity<FavoriteResponseDTO> addProduct(@PathVariable UUID favoriteId, @PathVariable UUID productId) {
        Favorite updated = favoriteService.addFavoriteToList(favoriteId, productId);
        return ResponseEntity.ok(mapToDTO(updated));
    }

    @Operation(summary = "Retirer un produit", description = "Retire un produit d'une liste de favoris.")
    @DeleteMapping("/{favoriteId}/products/{productId}")
    public ResponseEntity<FavoriteResponseDTO> removeProduct(@PathVariable UUID favoriteId, @PathVariable UUID productId) {
        Favorite updated = favoriteService.removeFavoriteFromList(favoriteId, productId);
        return ResponseEntity.ok(mapToDTO(updated));
    }

    @Operation(summary = "Supprimer une liste", description = "Supprime complètement une liste de favoris.")
    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> deleteList(@PathVariable UUID favoriteId) {
        boolean removed = favoriteService.removeFavoriteListById(favoriteId);
        if (removed) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

    // --- Helper de mapping ---
    private FavoriteResponseDTO mapToDTO(Favorite f) {
        List<ProductSummaryDTO> productDTOs = f.getProducts().stream()
                .map(p -> new ProductSummaryDTO(p.getId(), p.getName(), p.getPrice()))
                .collect(Collectors.toList());

        return new FavoriteResponseDTO(f.getId(), f.getLinkedUserId(), productDTOs);
    }
}