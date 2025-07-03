package com.FinZen.models.DTOS;

import java.time.LocalDate;

import lombok.Data;

@Data
public class InformeDto {

    private Long idInforme;
    private Long idUsuario; 
    private String nombreUsuario; 
    private LocalDate fechaGeneracion; 
    private String descripcion;
    private String tipoInforme;

}