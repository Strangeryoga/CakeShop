package com.stranger.major.model;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;
    private double price;
    private double weight;
    private String description;
    private String imageName;

    // Add a method to set the category to null before deleting the product
    public void removeCategory() {
        this.category = null;
    }
}
