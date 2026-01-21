package com.shophub.api.service;

import com.shophub.api.model.Product;
import com.shophub.api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        // TODO: Use pagination for better performance
        return productRepository.findAllWithDetails();
    }

    public Optional<Product> getProductById(UUID id) {
        return productRepository.findByIdWithDetails(id);
    }

    public List<Product> searchProducts(String query) {
        // TODO: Implement full-text search with proper indexing
        String lowerQuery = query.toLowerCase();
        return productRepository.findAllWithDetails().stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerQuery) ||
                           (p.getDescription() != null && p.getDescription().toLowerCase().contains(lowerQuery)))
                .toList();
    }

    public List<Product> filterProducts(String category, java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice) {
        return productRepository.findAllWithDetails().stream()
                .filter(p -> category == null || category.isEmpty() || 
                           (p.getCategory() != null && p.getCategory().getName().equalsIgnoreCase(category)))
                .filter(p -> minPrice == null || p.getPrice().compareTo(minPrice) >= 0)
                .filter(p -> maxPrice == null || p.getPrice().compareTo(maxPrice) <= 0)
                .toList();
    }
}
