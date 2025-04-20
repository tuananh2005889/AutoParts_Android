package com.BackEnd.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product {
    public Product(String name, String brand, String category, Double price,
                   int quantity, String description,
                   String compatibleVehicles, int yearOfManufacture,
                   String size, String material, double weight,
                   double discount, String warranty ) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.compatibleVehicles = compatibleVehicles;
        this.yearOfManufacture = yearOfManufacture;
        this.size = size;
        this.material = material;
        this.weight = weight;
        this.discount = discount;
        this.warranty = warranty;

    }
    public Product(){}
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false, length = 255)

    private String name;
    private String brand;
    private String category;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String compatibleVehicles;

    private Integer yearOfManufacture;
    private String size;
    private String material;
    private Double weight;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    private Double discount;
    private String warranty;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore // Bỏ qua serialize trường này
    private List<Review> reviews = new ArrayList<>();
}
