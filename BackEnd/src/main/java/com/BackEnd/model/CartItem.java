package com.BackEnd.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_item")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    public CartItem(Product product, int quantity, Cart cart){
        this.product = product;
        this.quantity = quantity;
        this.cart = cart;
    }
    @Id
    @Column(name = "cart_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name = "cart_id",  referencedColumnName = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id",  referencedColumnName = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

}
