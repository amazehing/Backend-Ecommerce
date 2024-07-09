package com.metinbudak.ecommerce.repository;

import com.metinbudak.ecommerce.repository.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
