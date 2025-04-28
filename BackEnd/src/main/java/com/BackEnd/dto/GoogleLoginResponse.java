package com.BackEnd.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleLoginResponse {
    private Long userId;
    private String token;
    private String userName;

    public GoogleLoginResponse(Long userId, String token, String userName) {
        this.userId = userId;
        this.token = token;
        this.userName = userName;
    }

}
