package com.BackEnd.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Cart> carts = new ArrayList<>();
    @Column(name = "user_name", nullable = false, length = 100, unique = true)
    private String userName;
    @Column(nullable = false, length = 255)
    private String password;
    @Column(nullable = false, length = 255)
    private String fullName;
    @Column(name = "gmail", nullable = false, length = 255, unique = true)
    private String gmail;
    @Column(length = 50)
    private String role;
    @Column(columnDefinition = "TEXT")
    private String address;
    @Column(length = 15, unique = true)
    private String phone;
//    @Column(name = "is_active")
//    private boolean isActive = true;
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User))
            return false;
        User user = (User) o;
        return Objects.equals(getUserName(), user.getUserName());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getUserName());
    }
}
