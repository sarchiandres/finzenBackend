package com.FinZen.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinZen.models.DTOS.IngresosDto;
import com.FinZen.models.Entities.Cuenta;
import com.FinZen.models.Entities.Ingresos;
import com.FinZen.models.Entities.Inversion;
import com.FinZen.models.Entities.Presupuesto;
import com.FinZen.models.Entities.Tarjeta;
import com.FinZen.repository.CuentaRepository;
import com.FinZen.repository.IngresosRepository;
import com.FinZen.repository.InversionRepository;
import com.FinZen.repository.PresupuestoRepository;
import com.FinZen.repository.TarjetaRepository;

@Service
public class IngresoServices {
    @Autowired
    private IngresosRepository ingresosRepository;
    @Autowired
    private CuentaRepository cuentaRepository;
     @Autowired
    private InversionRepository inversionRepository;
    @Autowired
    private TarjetaRepository tarjetaRepository;


    public Ingresos createIngreso(IngresosDto ingresoDto) {

        Cuenta miCuenta = null;
          Tarjeta miTarjeta = null;
          Inversion mInversion = null;
            if (ingresoDto.getIdInversion() != null && ingresoDto.getIdInversion() > 0) {
                    mInversion = inversionRepository.findById(ingresoDto.getIdInversion())
                        .orElseThrow(() -> new RuntimeException("La inversion no se encontro"));
            }

            if (ingresoDto.getIdCuenta() != null && ingresoDto.getIdCuenta() > 0) {
                    miCuenta = cuentaRepository.findById(ingresoDto.getIdCuenta())
                        .orElseThrow(() -> new RuntimeException("La cuenta no se encontro"));
            }
            if (ingresoDto.getIdTarjeta() != null && ingresoDto.getIdTarjeta() > 0) {
                    miTarjeta = tarjetaRepository.findById(ingresoDto.getIdTarjeta())
                        .orElseThrow(() -> new RuntimeException("La tarjeta no se encontro"));
            }

        Ingresos ingreso = new Ingresos();

        ingreso.setNombre(ingresoDto.getNombre());
        ingreso.setDescripcion(ingresoDto.getDescripcion());
        ingreso.setFecha(ingresoDto.getFecha());
        ingreso.setMonto(ingresoDto.getMonto());
        ingreso.setCuenta(miCuenta);
        ingreso.setTarjeta(miTarjeta);
        ingreso.setInversion(mInversion);

        return ingresosRepository.save(ingreso);
    }
    

    public List<Ingresos> getAllIngresos(Long idUsuario) {
        return ingresosRepository.findByUsuarioId(idUsuario);
    }



   public Ingresos updateIngreso(Long idIngreso, IngresosDto ingresoDto) {
        Ingresos ingreso = ingresosRepository.findById(idIngreso)
                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado"));

                Cuenta miCuenta = null;
                Tarjeta miTarjeta = null;
                Inversion mInversion = null;
                  if (ingresoDto.getIdInversion() != null && ingresoDto.getIdInversion() > 0) {
                          mInversion = inversionRepository.findById(ingresoDto.getIdInversion())
                              .orElseThrow(() -> new RuntimeException("La inversion no se encontro"));
                  }
      
                  if (ingresoDto.getIdCuenta() != null && ingresoDto.getIdCuenta() > 0) {
                          miCuenta = cuentaRepository.findById(ingresoDto.getIdCuenta())
                              .orElseThrow(() -> new RuntimeException("La cuenta no se encontro"));
                  }
                  if (ingresoDto.getIdTarjeta() != null && ingresoDto.getIdTarjeta() > 0) {
                          miTarjeta = tarjetaRepository.findById(ingresoDto.getIdTarjeta())
                              .orElseThrow(() -> new RuntimeException("La tarjeta no se encontro"));
                  }
      
              
      
              ingreso.setNombre(ingresoDto.getNombre());
              ingreso.setDescripcion(ingresoDto.getDescripcion());
              ingreso.setFecha(ingresoDto.getFecha());
              ingreso.setMonto(ingresoDto.getMonto());
              ingreso.setCuenta(miCuenta);
              ingreso.setTarjeta(miTarjeta);
              ingreso.setInversion(mInversion);
      
              return ingresosRepository.save(ingreso);
    }

    public String deleteIngreso(Long idIngreso) {
        Ingresos ingreso = ingresosRepository.findById(idIngreso)
                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado"));

        ingresosRepository.delete(ingreso);
        return "Ingreso eliminado con éxito";
    }
}
