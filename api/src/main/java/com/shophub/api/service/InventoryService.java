package com.shophub.api.service;

import com.shophub.api.model.Inventory;
import com.shophub.api.model.Product;
import com.shophub.api.repository.InventoryRepository;
import com.shophub.api.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public InventoryService(
            InventoryRepository inventoryRepository,
            ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void reduceStock(UUID productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        Inventory inventory = product.getInventory();
        if (inventory == null) {
            throw new RuntimeException("Inventory not found for product: " + productId);
        }

        int currentStock = inventory.getStock();
        if (currentStock < quantity) {
            throw new RuntimeException(
                    String.format("Insufficient stock. Available: %d, Requested: %d", currentStock, quantity));
        }

        inventory.setStock(currentStock - quantity);
        inventoryRepository.save(inventory);
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @Transactional
    public Inventory updateInventory(UUID productId, Inventory inventoryUpdate) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        Inventory inventory = product.getInventory();
        if (inventory == null) {
            throw new RuntimeException("Inventory not found for product: " + productId);
        }

        inventory.setStock(inventoryUpdate.getStock());
        inventory.setReservedStock(inventoryUpdate.getReservedStock());
        inventory.setLowStockThreshold(inventoryUpdate.getLowStockThreshold());
        
        return inventoryRepository.save(inventory);
    }

    public List<Inventory> getLowStockItems() {
        return inventoryRepository.findAll().stream()
                .filter(inv -> inv.getStock() <= inv.getLowStockThreshold())
                .collect(Collectors.toList());
    }
}
