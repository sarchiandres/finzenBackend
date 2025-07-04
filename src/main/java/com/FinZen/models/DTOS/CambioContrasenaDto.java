// src/main/java/com/FinZen/models/DTOS/CambioContrasenaDto.java
package com.FinZen.models.DTOS;

import lombok.Data;

@Data
public class CambioContrasenaDto {
    private String currentPassword;
    private String newPassword;
}