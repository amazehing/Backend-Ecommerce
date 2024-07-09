package com.metinbudak.ecommerce.repository;

import com.metinbudak.ecommerce.repository.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
