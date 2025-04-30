package com.BackEnd.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", unique = true, nullable = false)
    private Order order;

    @Column(nullable = false, length = 100)
    private String paymentMethod;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime paymentDate;

    @Column(nullable = false, length = 50)
    private String paymentStatus = "Pending";

    @Column(nullable = false)
    private double amount; // Số tiền thanh toán
}
