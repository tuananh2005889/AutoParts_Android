package com.BackEnd.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
// Nếu bạn giữ tên bảng là "user" (MySQL coi user là từ khóa), hãy quote nó lại
// hoặc đổi tên bảng thành "users" trong database và ở đây thành @Table(name =
// "users")
@Table(name = "`users`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private Long userId;

    @Column(name = "user_name", nullable = false, length = 100, unique = true)
    private String userName;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "gmail", nullable = false, length = 255, unique = true)
    private String gmail;

    @Column(length = 50)
    private String role;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 15, unique = true)
    private String phone;

    @Column(name = "avatar_url", columnDefinition = "TEXT")
    private String avatarUrl;

}
