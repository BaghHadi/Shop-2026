package fr.esiea.shop2026.usecase.service;

import fr.esiea.shop2026.domain.entities.Product;
import fr.esiea.shop2026.domain.exception.NotFoundException;
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
            // IllegalArgumentException est plus standard java pour un argument invalide
            throw new IllegalArgumentException("Price cannot be negative");
        }

        if (product.getId() == null) {
            product.setId(UUID.randomUUID());
        }

        return productRepository.save(product);
    }

    public void modifyProduct(Product product) {
        if (productRepository.findById(product.getId()).isEmpty()) {
            // ✅ NotFoundException
            throw new NotFoundException("Product", product.getId());
        }
        productRepository.save(product);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    public Product getProduct(UUID id) {
        // ✅ NotFoundException
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product", id));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(UUID id, Product newInfos) {
        // 1. On cherche le produit existant (lance NotFoundException si absent)
        Product existingProduct = getProduct(id);

        // 2. On met à jour les champs
        existingProduct.setName(newInfos.getName());
        existingProduct.setDescription(newInfos.getDescription());
        existingProduct.setPrice(newInfos.getPrice());
        existingProduct.setStockQuantity(newInfos.getStockQuantity());

        // 3. On sauvegarde
        return productRepository.save(existingProduct);
    }
}