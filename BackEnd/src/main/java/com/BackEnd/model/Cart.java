package com.BackEnd.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "cart")
@Getter
@Setter
@AllArgsConstructor
public class Cart {
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    /*
    khi fetch cart tu db, jpa/hibernate tu dong load user(due to ManyToOne default FetchType.EAGER)
    Neu muon fetch cart ma khong load user(chi getUser luc can thiet), ta dung FetchType.Lazy
    */

    //@ManyToOne(optional = false) if user is deleted, JPA throw exception
    //@OnDelete(action = OnDeleteAction.CASCADE) automatically delete cart if user is deleted
//    @ManyToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
//    private User user;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    /*
    Inverse side (không quản lý khóa ngoại),
    Owner side: CartItem, vì nó chứa khóa ngoại

    **Thao tác thêm/xóa quan hệ phải bắt đầu từ owning side (CartItem) để đảm bảo dữ liệu nhất quán
    */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> cartItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    public enum CartStatus {
        ACTIVE,
        PAID,
        CANCELLED
    }
    public Cart() {
        this.status = CartStatus.ACTIVE;
    }




}
