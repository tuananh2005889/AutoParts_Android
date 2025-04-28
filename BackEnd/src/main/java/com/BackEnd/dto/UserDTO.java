package com.BackEnd.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long userId;
    private String fullName;
    private String userName;
    private String password;
    private String gmail;
    private String address;
    private String role;
    private String phone;
    private List<CartDTO> carts;
    private String avatarUrl;
}
