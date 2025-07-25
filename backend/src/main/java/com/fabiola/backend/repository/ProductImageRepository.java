package com.fabiola.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fabiola.backend.entities.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {
    // You can add custom query methods here if needed
}