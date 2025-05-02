package com.BackEnd.dto;

import lombok.Data;

@Data
public class UpdateUserInfoRequest {
    private String userName;
    private String fullName;
    private String password;
    private String gmail;
    private String address;
    private String phone;
}
