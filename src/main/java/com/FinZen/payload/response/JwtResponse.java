package com.FinZen.payload.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long idUsuario;
    private String nombreUsuario;
    private String correo;
    private String role;

    public JwtResponse(String token, Long idUsuario, String nombreUsuario, String correo, String role) {
        this.token = token;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.role = role;
    }
}
