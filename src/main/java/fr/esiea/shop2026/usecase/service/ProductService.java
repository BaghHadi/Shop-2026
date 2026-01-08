package fr.esiea.shop2026.usecase.service;

import fr.esiea.shop2026.domain.entities.Product;
import fr.esiea.shop2026.domain.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        if (product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Price cannot be negative");
        }

        if (product.getId() == null) {
            product.setId(UUID.randomUUID());
        }

        return productRepository.save(product);
    }

    public void modifyProduct(Product product) {
        if (productRepository.findById(product.getId()).isEmpty()) {
            throw new RuntimeException("Product not found");
        }
        productRepository.save(product);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    public Product getProduct(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Product updateProduct(UUID id, Product newInfos) {
        // 1. On cherche le produit existant
        Product existingProduct = getProduct(id); // (Utilise ta méthode getProduct qui lance une exception si pas trouvé)

        // 2. On met à jour les champs
        existingProduct.setName(newInfos.getName());
        existingProduct.setDescription(newInfos.getDescription());
        existingProduct.setPrice(newInfos.getPrice());
        existingProduct.setStockQuantity(newInfos.getStockQuantity());

        // 3. On sauvegarde (JPA va comprendre que c'est une mise à jour car l'ID existe déjà)
        return productRepository.save(existingProduct);
    }
}