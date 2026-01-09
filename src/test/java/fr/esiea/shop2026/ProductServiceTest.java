package fr.esiea.shop2026;

import fr.esiea.shop2026.domain.entities.Product;
import fr.esiea.shop2026.domain.exception.NotFoundException;
import fr.esiea.shop2026.domain.repository.ProductRepository;
import fr.esiea.shop2026.usecase.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_Success() {
        // Arrange
        Product newProduct = new Product(null, "T-Shirt", "Cool", BigDecimal.valueOf(20), 10);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Product created = productService.createProduct(newProduct);

        // Assert
        assertNotNull(created.getId()); // Vérifie que l'ID a été généré
        assertEquals("T-Shirt", created.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_NegativePrice_ThrowsException() {
        // Arrange
        Product badProduct = new Product(null, "Bad", "Desc", BigDecimal.valueOf(-5), 10);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(badProduct));
        verify(productRepository, never()).save(any());
    }

    @Test
    void getProduct_NotFound_ThrowsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productService.getProduct(id));
    }

    @Test
    void updateProduct_Success() {
        // Arrange
        UUID id = UUID.randomUUID();
        Product existing = new Product(id, "Old Name", "Desc", BigDecimal.TEN, 5);
        Product updates = new Product(id, "New Name", "Desc", BigDecimal.TEN, 5);

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenReturn(updates);

        // Act
        Product result = productService.updateProduct(id, updates);

        // Assert
        assertEquals("New Name", result.getName());
        verify(productRepository).save(existing);
    }
}