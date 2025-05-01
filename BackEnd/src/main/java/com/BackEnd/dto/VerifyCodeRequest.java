package com.BackEnd.dto;

import lombok.Data;

@Data
public class VerifyCodeRequest {
    private String gmail;
    private String code;
}
