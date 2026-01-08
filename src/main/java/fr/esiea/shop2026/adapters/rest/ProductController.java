package fr.esiea.shop2026.adapters.rest;

import fr.esiea.shop2026.domain.entities.Product;
import fr.esiea.shop2026.usecase.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products") // URL: http://localhost:8080/products
@Tag(name = "Catalogue Produits", description = "Opérations liées à la gestion des articles (Ajout, modification, consultation)")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // POST /products (Créer un produit)
    @Operation(summary = "Créer un produit", description = "Ajoute un nouveau produit au catalogue avec son stock et son prix.")
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ResponseEntity.ok(created);
    }

    // GET /products/{id} (Récupérer un produit)
    @Operation(summary = "Récupérer un produit", description = "Retourne les détails d'un produit spécifique via son ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(productService.getProduct(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /products (Récupérer tous les produits)
    @Operation(summary = "Lister les produits", description = "Récupère la liste complète de tous les produits disponibles en base.")
    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    // DELETE /products/{id} (Supprimer un produit)
    @Operation(summary = "Supprimer un produit", description = "Retire définitivement un produit du catalogue.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Modifier un produit", description = "Met à jour les informations (nom, prix, stock) d'un produit existant.")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @RequestBody Product product) {
        try {
            Product updated = productService.updateProduct(id, product);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}