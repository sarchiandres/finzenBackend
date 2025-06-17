package com.FinZen.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinZen.models.DTOS.PresupuestoDto;
import com.FinZen.models.Entities.CategoriaPresupuesto;
import com.FinZen.models.Entities.Cuenta;
import com.FinZen.models.Entities.Inversion;
import com.FinZen.models.Entities.Presupuesto;
import com.FinZen.models.Entities.Tarjeta;
import com.FinZen.repository.CategoriaPresupuestoRepository;
import com.FinZen.repository.CuentaRepository;
import com.FinZen.repository.InversionRepository;
import com.FinZen.repository.PresupuestoRepository;
import com.FinZen.repository.TarjetaRepository;

import jakarta.transaction.Transactional;

@Service
public class PresupuestoServices {
    @Autowired
    private PresupuestoRepository presupuestoRepository;
    @Autowired
    private CuentaRepository cuentaRepository;
    @Autowired
    private CategoriaPresupuestoRepository categoriaPresupuestoRepository;
    @Autowired
    private InversionRepository inversionRepository;
    @Autowired
    private TarjetaRepository tarjetaRepository;

    public Presupuesto savePresupuesto(PresupuestoDto presupuestoDto) {
          Cuenta miCuenta = null;
          Tarjeta miTarjeta = null;
          Inversion mInversion = null;
            if (presupuestoDto.getIdInversion() != null && presupuestoDto.getIdInversion() > 0) {
                    mInversion = inversionRepository.findById(presupuestoDto.getIdInversion())
                        .orElseThrow(() -> new RuntimeException("La inversion no se encontro"));
            }

            if (presupuestoDto.getIdCuenta() != null && presupuestoDto.getIdCuenta() > 0) {
                    miCuenta = cuentaRepository.findById(presupuestoDto.getIdCuenta())
                        .orElseThrow(() -> new RuntimeException("La cuenta no se encontro"));
            }
            if (presupuestoDto.getIdTarjeta() != null && presupuestoDto.getIdTarjeta() > 0) {
                    miTarjeta = tarjetaRepository.findById(presupuestoDto.getIdTarjeta())
                        .orElseThrow(() -> new RuntimeException("La tarjeta no se encontro"));
            }

            
            
            CategoriaPresupuesto categoria = categoriaPresupuestoRepository.findById(presupuestoDto.getIdCategoriaPresupuesto())
                        .orElseThrow(() -> new RuntimeException("La categoria no se ha encontrado"));

                Presupuesto miPresupuesto = new Presupuesto();

                miPresupuesto.setCuenta(miCuenta);
                miPresupuesto.setNombre(presupuestoDto.getNombre());
                miPresupuesto.setMontoAsignado(presupuestoDto.getMontoAsignado());
                miPresupuesto.setCategoria(categoria);
                miPresupuesto.setTarjeta(miTarjeta);
                miPresupuesto.setInversion(mInversion);
        
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
