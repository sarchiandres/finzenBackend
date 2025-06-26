package com.FinZen.models.DTOS;

import com.FinZen.models.Entities.CategoriaPresupuesto;
import com.FinZen.models.Entities.Cuenta;
import com.FinZen.models.Entities.Inversion;
import com.FinZen.models.Entities.Tarjeta;
import lombok.Data; 
import java.math.BigDecimal;

@Data 
public class PresupuestoResponseDto {

    private Long idPresupuesto; 
    private String nombre; 
    private BigDecimal montoAsignado; 

    
    private BigDecimal montoGastado;
    private Cuenta cuenta;
    private Tarjeta tarjeta;
    private Inversion inversion;
    private CategoriaPresupuesto categoria;

    
    public PresupuestoResponseDto() {
    }
    public PresupuestoResponseDto(
            Long idPresupuesto,
            String nombre,
            BigDecimal montoAsignado,
            Cuenta cuenta,
            Tarjeta tarjeta,
            Inversion inversion,
            CategoriaPresupuesto categoria,
            BigDecimal montoGastado) { 
        this.idPresupuesto = idPresupuesto;
        this.nombre = nombre;
        this.montoAsignado = montoAsignado;
        this.cuenta = cuenta;
        this.tarjeta = tarjeta;
        this.inversion = inversion;
        this.categoria = categoria;
        this.montoGastado = montoGastado; 
    }

   
}