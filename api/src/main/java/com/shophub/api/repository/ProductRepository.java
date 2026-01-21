package com.shophub.api.repository;

import com.shophub.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p FROM Product p JOIN FETCH p.category JOIN FETCH p.inventory WHERE p.category.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") UUID categoryId);
    
    @Query("SELECT p FROM Product p JOIN FETCH p.category JOIN FETCH p.inventory")
    List<Product> findAllWithDetails();
    
    @Query("SELECT p FROM Product p JOIN FETCH p.category JOIN FETCH p.inventory WHERE p.id = :id")
    java.util.Optional<Product> findByIdWithDetails(@Param("id") UUID id);
}
