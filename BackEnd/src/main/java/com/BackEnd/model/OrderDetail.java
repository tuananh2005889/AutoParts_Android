package com.BackEnd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_detail")
@Getter
@Setter
public class OrderDetail {
    @Id
    @Column(name = "order_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false,
            referencedColumnName = "order_id"
    )
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id",
            referencedColumnName = "product_id",
            nullable = false
    )
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private Double totalPrice;

    public OrderDetail(Order order, Product product, int quantity, Double totalPrice) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public OrderDetail() {

    }
}
