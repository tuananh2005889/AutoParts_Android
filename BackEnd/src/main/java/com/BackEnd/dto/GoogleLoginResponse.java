package com.BackEnd.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleLoginResponse {
    private String token;
    private String userName;

    public GoogleLoginResponse(String token, String userName) {
        this.token = token;
        this.userName = userName;
    }

}
