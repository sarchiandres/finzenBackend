package com.FinZen.models.DTOS;

import lombok.Data;

@Data
public class CategoriaGastoConConteoDTO {
    private Long idCategoriaGasto;
    private String nombreCategoria;
    private Long conteoGastos;

    public CategoriaGastoConConteoDTO(Long idCategoria, String nombre, Long conteo) {
        this.idCategoriaGasto = idCategoria;
        this.nombreCategoria = nombre;
        this.conteoGastos = conteo;
    }
}
