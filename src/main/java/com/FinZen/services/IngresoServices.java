package com.FinZen.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinZen.models.DTOS.IngresosDto;
import com.FinZen.models.Entities.Ingresos;
import com.FinZen.models.Entities.Presupuesto;
import com.FinZen.repository.IngresosRepository;
import com.FinZen.repository.PresupuestoRepository;

@Service
public class IngresoServices {
    @Autowired
    private IngresosRepository ingresosRepository;
    @Autowired
    private PresupuestoRepository PresupuestoRepository;


    public Ingresos createIngreso(IngresosDto ingresoDto) {

        Presupuesto presupuesto = PresupuestoRepository.findById(ingresoDto.getIdPresupuesto())
                .orElseThrow(() -> new RuntimeException("Presupuesto no extiste"));

        Ingresos ingreso = new Ingresos();

        ingreso.setNombre(ingresoDto.getNombre());
        ingreso.setFuente(ingresoDto.getFuente());
        ingreso.setFecha(ingresoDto.getFecha());
        ingreso.setMonto(ingresoDto.getMonto());
        ingreso.setPresupuesto(presupuesto);

        return ingresosRepository.save(ingreso);
    }
    

    public List<Ingresos> getAllIngresos(Long idPresupuesto) {
        return ingresosRepository.findByPresupuestoIdPresupuesto(idPresupuesto);
    }

   public Ingresos updateIngreso(Long idIngreso, IngresosDto ingresoDto) {
        Ingresos ingreso = ingresosRepository.findById(idIngreso)
                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado"));

        Presupuesto presupuesto = PresupuestoRepository.findById(ingresoDto.getIdPresupuesto())
                .orElseThrow(() -> new RuntimeException("No se encontró el presupuesto con ID: " + ingresoDto.getIdPresupuesto()));

        ingreso.setNombre(ingresoDto.getNombre());
        ingreso.setFuente(ingresoDto.getFuente());
        ingreso.setFecha(ingresoDto.getFecha());
        ingreso.setPresupuesto(presupuesto);

        return ingresosRepository.save(ingreso);
    }

    public String deleteIngreso(Long idIngreso) {
        Ingresos ingreso = ingresosRepository.findById(idIngreso)
                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado"));

        ingresosRepository.delete(ingreso);
        return "Ingreso eliminado con éxito";
    }
}
