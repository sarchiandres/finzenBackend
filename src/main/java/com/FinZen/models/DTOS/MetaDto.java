package com.FinZen.models.DTOS;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MetaDto {

    private Long idMeta;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no puede exceder 100 caracteres")
    private String titulo;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @NotNull(message = "El estado es obligatorio")
    private String estado; // "creado", "iniciado", "terminado"

    @NotNull(message = "El valor es obligatorio")
    @DecimalMin(value = "0.01", message = "El valor debe ser mayor a 0")
    private BigDecimal valor;

    @DecimalMin(value = "0.00", message = "El monto ahorrado no puede ser negativo")
    private BigDecimal montoAhorrado = BigDecimal.ZERO;

    @NotNull(message = "El ID de cuenta es obligatorio")
    private Long idCuenta;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaLimite;

    private Boolean enProgreso = false;

    @Size(max = 10, message = "El icono no puede exceder 10 caracteres")
    private String icon;

    // Constructores
    public MetaDto() {}

    public MetaDto(String titulo, String descripcion, String estado, BigDecimal valor,
                   Long idCuenta, LocalDate fechaInicio, LocalDate fechaLimite, String icon) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.valor = valor;
        this.idCuenta = idCuenta;
        this.fechaInicio = fechaInicio;
        this.fechaLimite = fechaLimite;
        this.icon = icon;
        this.montoAhorrado = BigDecimal.ZERO;
        this.enProgreso = false;
    }

    // Getters y Setters
    public Long getIdMeta() {
        return idMeta;
    }

    public void setIdMeta(Long idMeta) {
        this.idMeta = idMeta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getMontoAhorrado() {
        return montoAhorrado;
    }

    public void setMontoAhorrado(BigDecimal montoAhorrado) {
        this.montoAhorrado = montoAhorrado;
    }

    public Long getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(Long idCuenta) {
        this.idCuenta = idCuenta;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Boolean getEnProgreso() {
        return enProgreso;
    }

    public void setEnProgreso(Boolean enProgreso) {
        this.enProgreso = enProgreso;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    // Métodos de utilidad
    public BigDecimal calcularPorcentajeCompletado() {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return montoAhorrado.divide(valor, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public boolean isCompletada() {
        return "terminado".equals(estado) ||
                (montoAhorrado != null && valor != null &&
                        montoAhorrado.compareTo(valor) >= 0);
    }

    @Override
    public String toString() {
        return "MetaDto{" +
                "idMeta=" + idMeta +
                ", titulo='" + titulo + '\'' +
                ", estado='" + estado + '\'' +
                ", valor=" + valor +
                ", montoAhorrado=" + montoAhorrado +
                ", idCuenta=" + idCuenta +
                ", fechaInicio=" + fechaInicio +
                ", fechaLimite=" + fechaLimite +
                '}';
    }
}