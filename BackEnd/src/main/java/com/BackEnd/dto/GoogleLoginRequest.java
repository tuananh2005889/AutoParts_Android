package com.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class GoogleLoginRequest {
    private String idToken;

    public String getIdToken() {
        return idToken;
    }

}
