package com.FinZen.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinZen.models.DTOS.GastosDto;
import com.FinZen.models.DTOS.GastosResponseDto;
import com.FinZen.models.Entities.CategoriaGasto;
import com.FinZen.models.Entities.Gastos;
import com.FinZen.models.Entities.Presupuesto;
import com.FinZen.repository.CategoriaGastoRepository;
import com.FinZen.repository.GastosRepository;
import com.FinZen.repository.PresupuestoRepository;

@Service
public class GastosServices {
    
    @Autowired
    private GastosRepository gastosRepository;
    @Autowired
    private PresupuestoRepository presupuestoRepository;
    @Autowired
    private CategoriaGastoRepository categoriaGastoRepository;

    public Gastos saveGasto (GastosDto gastosDto){
        Presupuesto presupuesto = presupuestoRepository.findById(gastosDto.getIdPresupuesto())
                .orElseThrow(() -> new RuntimeException("No se encontró el presupuesto con ID: " + gastosDto.getIdPresupuesto()));

        CategoriaGasto categoriaGasto = categoriaGastoRepository.findById(gastosDto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("No se encontró la categoría de gasto con ID: " + gastosDto.getIdCategoria()));

        Gastos gasto = new Gastos();
        gasto.setNombre(gastosDto.getNombre());
        gasto.setMonto(gastosDto.getMonto());
        gasto.setDescripcion(gastosDto.getDescripcion());
        gasto.setFecha(gastosDto.getFecha());
        gasto.setCategoria(categoriaGasto);
        gasto.setPresupuesto(presupuesto);

        return gastosRepository.save(gasto);
    }

    public List<GastosResponseDto> getGastosForUser(Long userId) {
        List<Gastos> gastosEntities = gastosRepository.getGastosByUsuarioId(userId);

        return gastosEntities.stream()
            .map(gasto -> new GastosResponseDto(
                gasto.getIdGasto(),
                gasto.getNombre(),
                gasto.getMonto(),
                gasto.getDescripcion(),
                gasto.getFecha(),
                gasto.getCategoria() != null ? gasto.getCategoria().getIdCategoria() : null,
                gasto.getCategoria() != null ? gasto.getCategoria().getNombre() : "Sin Categoría",
                gasto.getPresupuesto() != null ? gasto.getPresupuesto().getIdPresupuesto() : null,
                gasto.getPresupuesto() != null ? gasto.getPresupuesto().getNombre() : null
            ))
            .collect(Collectors.toList());
    }

    public Gastos updateGasto (Long idGasto, GastosDto gastosDto){
        Gastos gasto = gastosRepository.findById(idGasto)
                .orElseThrow(() -> new RuntimeException("No se encontró el gasto con ID: " + idGasto));

        Presupuesto presupuesto = presupuestoRepository.findById(gastosDto.getIdPresupuesto())
                .orElseThrow(() -> new RuntimeException("No se encontró el presupuesto con ID: " + gastosDto.getIdPresupuesto()));

        CategoriaGasto categoriaGasto = categoriaGastoRepository.findById(gastosDto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("No se encontró la categoría de gasto con ID: " + gastosDto.getIdCategoria()));

        gasto.setNombre(gastosDto.getNombre());
        gasto.setMonto(gastosDto.getMonto());
        gasto.setDescripcion(gastosDto.getDescripcion());
        gasto.setCategoria(categoriaGasto);
        gasto.setFecha(gastosDto.getFecha());
        gasto.setPresupuesto(presupuesto);
        return gastosRepository.save(gasto);
    }

    public String deleteGasto (Long idGasto){
        if (!gastosRepository.existsById(idGasto)) {
            throw new RuntimeException("El gasto con ID: " + idGasto + " no existe.");
        }
        try {
            gastosRepository.deleteById(idGasto);
            return "Gasto eliminado con éxito";
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el gasto: " + e.getMessage());
        }
    }

    public List<Gastos> getGastosByPresupuestoId(Long idPresupuesto) {
        return gastosRepository.findByPresupuestoIdPresupuesto(idPresupuesto);
    }
}