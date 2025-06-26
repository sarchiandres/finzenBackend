package com.FinZen.models.DTOS;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TarjetaDto {
    private Long idUsuario;
    private String nombre;
    private String tipo; // CREDITO o DEBITO
    private String banco;
    private String numeroUltimosDigitos;
    private BigDecimal saldoActual;
    private BigDecimal limiteCredito;
}