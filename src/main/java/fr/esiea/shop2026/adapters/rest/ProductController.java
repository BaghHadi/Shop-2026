package fr.esiea.shop2026.adapters.rest;

import fr.esiea.shop2026.domain.entities.Product;
import fr.esiea.shop2026.usecase.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products") // URL: http://localhost:8080/products
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // POST /products (Créer un produit)
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ResponseEntity.ok(created);
    }

    // GET /products/{id} (Récupérer un produit)
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(productService.getProduct(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /products (Récupérer tous les produits)
    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    // DELETE /products/{id} (Supprimer un produit)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}