package com.FinZen.models.DTOS;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UsuarioDto {

    private String nombre;
    private String correo;
    private String contrasena;
    private Long numeroDocumento;
    private String paisResidencia;
    private Long ingresoMensual;
    private Boolean metaActual;
    private String nombreUsuario;
    private String tipoDocumento;
    private String tipoPersona;
    private MultipartFile urlImg;
    private String tipoUsuario;

}