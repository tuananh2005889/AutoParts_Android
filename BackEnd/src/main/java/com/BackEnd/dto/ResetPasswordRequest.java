package com.BackEnd.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String gmail;
    private String newPassword;
}
