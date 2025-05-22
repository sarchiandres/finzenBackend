package com.FinZen.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinZen.models.DTOS.DeudaDto;
import com.FinZen.models.Entities.Cuenta;
import com.FinZen.models.Entities.Deuda;
import com.FinZen.repository.CuentaRepository;
import com.FinZen.repository.DeudaRepository;

@Service
public class DeudaServices {
    
    @Autowired
    private DeudaRepository deudaRepository;
    @Autowired
    private CuentaRepository cuentaRepository;


    public Deuda createDeuda(DeudaDto deudaDto) {
        if (deudaRepository.findByNombre(deudaDto.getNombre()).isPresent()) {
            throw new RuntimeException("Deuda con el mismo nombre ya existe");
            
        }
        Cuenta cuenta = cuentaRepository.findById(deudaDto.getIdCuenta())
        .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        Deuda deuda = new Deuda();
        deuda.setNombre(deudaDto.getNombre());
        deuda.setMonto(deudaDto.getMonto());
        deuda.setMontoPagado(deudaDto.getMontoPagado());
        deuda.setFechaVencimiento(deudaDto.getFechaVencimiento());
        deuda.setEstado(deudaDto.getEstado());
        deuda.setFechaCreacion(LocalDateTime.now());       
        deuda.setCuenta(cuenta);
        
        return deudaRepository.save(deuda);
    }

    public Deuda updateDeuda(Long idDeuda, DeudaDto deudaDto) {
        Deuda deuda = deudaRepository.findById(idDeuda)
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));
        
        deuda.setNombre(deudaDto.getNombre());
        deuda.setMonto(deudaDto.getMonto());
        deuda.setMontoPagado(deudaDto.getMontoPagado());
        deuda.setFechaVencimiento(deudaDto.getFechaVencimiento());
        deuda.setEstado(deudaDto.getEstado());
        
        return deudaRepository.save(deuda);
    }

    public List<Deuda> getDeudasByCuenta(Long idCuenta) {
        return deudaRepository.findByCuentaIdCuenta(idCuenta);
    }

    public void deleteDeuda(Long idDeuda) {
        Deuda deuda = deudaRepository.findById(idDeuda)
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));
        deudaRepository.delete(deuda);
    }
}
