package com.FinZen.payload;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String token;
    private String nuevaContrasena;
}
