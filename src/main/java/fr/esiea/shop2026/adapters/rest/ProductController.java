package fr.esiea.shop2026.adapters.rest;

import fr.esiea.shop2026.domain.entities.Product;
import fr.esiea.shop2026.usecase.dto.ProductRequestDTO;
import fr.esiea.shop2026.usecase.dto.ProductResponseDTO;
import fr.esiea.shop2026.usecase.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@Tag(name = "Catalogue Produits", description = "Opérations liées à la gestion des articles (Ajout, modification, consultation)")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // POST /products
    @Operation(summary = "Créer un produit", description = "Ajoute un nouveau produit au catalogue avec son stock et son prix.")
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductRequestDTO dto) {
        Product productToCreate = new Product(null, dto.name, dto.description, dto.price, dto.stockQuantity);
        Product created = productService.createProduct(productToCreate);
        return ResponseEntity.ok(mapToResponse(created));
    }

    // GET /products/{id}
    @Operation(summary = "Récupérer un produit", description = "Retourne les détails d'un produit spécifique via son ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable UUID id) {
        // ✅ Pas de try-catch : NotFoundException sera géré par GlobalExceptionHandler
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(mapToResponse(product));
    }

    // GET /products
    @Operation(summary = "Lister les produits", description = "Récupère la liste complète de tous les produits disponibles en base.")
    @GetMapping
    public List<ProductResponseDTO> getAll() {
        return productService.getAllProducts().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // DELETE /products/{id}
    @Operation(summary = "Supprimer un produit", description = "Retire définitivement un produit du catalogue.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // PUT /products/{id}
    @Operation(summary = "Modifier un produit", description = "Met à jour les informations (nom, prix, stock) d'un produit existant.")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable UUID id, @RequestBody ProductRequestDTO dto) {
        // ✅ Pas de try-catch
        Product productInfos = new Product(null, dto.name, dto.description, dto.price, dto.stockQuantity);
        Product updated = productService.updateProduct(id, productInfos);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    private ProductResponseDTO mapToResponse(Product p) {
        return new ProductResponseDTO(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getStockQuantity());
    }
}