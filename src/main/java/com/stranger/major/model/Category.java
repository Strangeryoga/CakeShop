package com.stranger.major.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private int id;

    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    // Constructors, getters, setters, etc.

    // Add a method to handle the removal of associated products
    public void removeProducts() {
        for (Product product : products) {
            // Set the category to null for each associated product
            product.setCategory(null);
        }
        // Clear the list of associated products
        products.clear();
    }
}
