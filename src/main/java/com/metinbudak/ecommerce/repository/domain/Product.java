package com.metinbudak.ecommerce.repository.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double new_price;
    private double old_price;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private Set<Image> images = new LinkedHashSet<>();

    public Product() {
    }

    public Product(Category category, String name, double new_price, double old_price, Set<Image> images) {
        this.category = category;
//        this.category.addProduct(this);
        this.name = name;
        this.new_price = new_price;
        this.old_price = old_price;
        this.images = images;
    }

    public Product(Category category, String name, double new_price, double old_price, Image image) {
        this.category = category;
//        this.category.addProduct(this);
        this.name = name;
        this.new_price = new_price;
        this.old_price = old_price;
        this.images = Set.of(image);
    }

}
