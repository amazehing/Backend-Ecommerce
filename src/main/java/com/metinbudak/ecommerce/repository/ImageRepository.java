package com.metinbudak.ecommerce.repository;

import com.metinbudak.ecommerce.repository.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByLocation(String location);

}
