package com.FinZen.models.DTOS;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class InversionDto {
    private Long idUsuario;
    private String nombre;
    private String tipoInversion; // ACCIONES, FONDOS, CRYPTO
    private String plataforma;
    private BigDecimal valorInicial;
    private BigDecimal valorActual;
    private LocalDate fechaInicio;
}