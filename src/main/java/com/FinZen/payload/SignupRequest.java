package com.FinZen.payload;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class SignupRequest {
    private String nombre;
    private String correo;
    private String contrasena;
    private Long numeroDocumento;
    private String tipoDocumento;
    private String paisResidencia;
    private Long ingresoMensual;
    private Boolean metaActual;
    private String nombreUsuario;
    private String tipoPersona;
    private MultipartFile urlImg;
    private String role;
    
}
