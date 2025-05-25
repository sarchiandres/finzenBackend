package com.FinZen.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class LoginRequest {

    @NotBlank
    private String correo;

    @NotBlank
    private String nombreUsuario;

    @NotBlank
    private String contrasena;

}
