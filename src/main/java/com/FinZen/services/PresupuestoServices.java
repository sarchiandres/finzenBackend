package com.FinZen.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinZen.models.DTOS.PresupuestoDto;
import com.FinZen.models.Entities.CategoriaPresupuesto;
import com.FinZen.models.Entities.Cuenta;
import com.FinZen.models.Entities.Presupuesto;
import com.FinZen.repository.CategoriaPresupuestoRepository;
import com.FinZen.repository.CuentaRepository;
import com.FinZen.repository.PresupuestoRepository;

import jakarta.transaction.Transactional;

@Service
public class PresupuestoServices {
    @Autowired
    private PresupuestoRepository presupuestoRepository;
    @Autowired
    private CuentaRepository cuentaRepository;
    @Autowired
    private CategoriaPresupuestoRepository categoriaPresupuestoRepository;

    public Presupuesto savePresupuesto(PresupuestoDto presupuestoDto) {
          Cuenta cuenta = cuentaRepository.findById(presupuestoDto.getIdCuenta())
                .orElseThrow(()-> new RuntimeException("la cuenta no se encontro "));

        CategoriaPresupuesto categoria = categoriaPresupuestoRepository.findById(presupuestoDto.getIdCategoriaPresupuesto())
                .orElseThrow(()->new RuntimeException("La cateforia no se encontro"));

                Presupuesto miPresupuesto = new Presupuesto();

                miPresupuesto.setCuenta(cuenta);
                miPresupuesto.setNombre(presupuestoDto.getNombre());
                miPresupuesto.setMontoAsignado(presupuestoDto.getMontoAsignado());
                miPresupuesto.setCategoria(categoria);
        
                return presupuestoRepository.save(miPresupuesto);
    }




    public Presupuesto updatePresupuesto (long idPresupuesto, PresupuestoDto presupuestoDto){
        Presupuesto miPresuspuesto  = presupuestoRepository.findById(idPresupuesto)
                .orElseThrow(()-> new RuntimeException("El presupuesto no se encontro "));
        CategoriaPresupuesto categoria = categoriaPresupuestoRepository.findById(presupuestoDto.getIdCategoriaPresupuesto())
                .orElseThrow(()-> new RuntimeException("La categoria no se ha encontrado"));
        Cuenta cuenta = cuentaRepository.findById(presupuestoDto.getIdCuenta())
                .orElseThrow(()->new RuntimeException("La cuenta no se encontro"));

        miPresuspuesto.setCategoria(categoria);
        miPresuspuesto.setCuenta(cuenta);
        miPresuspuesto.setNombre(presupuestoDto.getNombre());
        miPresuspuesto.setMontoAsignado(presupuestoDto.getMontoAsignado());

        return presupuestoRepository.save(miPresuspuesto);
    }

    @Transactional
    public List<Presupuesto> getPresupuestos(Long idCuenta){
        return  presupuestoRepository.findByCuentaIdCuenta(idCuenta);
    }


    public String deletePresupuesto(Long idPresupuesto){
        try {
            presupuestoRepository.deleteById(idPresupuesto);
            return "Presupuesto eliminado con éxito";
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el presupuesto: " + e.getMessage());
        }
    }
    
}
