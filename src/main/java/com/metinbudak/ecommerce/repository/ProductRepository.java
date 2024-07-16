package com.metinbudak.ecommerce.repository;

import com.metinbudak.ecommerce.repository.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCategoryId(long categoryId);

    Optional<Product> findByCategoryIdAndName(long categoryId, String name);
}
