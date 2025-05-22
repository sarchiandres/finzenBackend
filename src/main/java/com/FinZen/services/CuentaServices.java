package com.FinZen.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinZen.models.DTOS.CuentaDto;
import com.FinZen.models.Entities.Cuenta;
import com.FinZen.models.Entities.Usuarios;
import com.FinZen.repository.CuentaRepository;
import com.FinZen.repository.UsuariosRepository;

@Service
public class CuentaServices {
    
    @Autowired
    private CuentaRepository cuentaRepository;
    @Autowired
    private UsuariosRepository usuariosRepository;

    public Cuenta createAccount(CuentaDto cuentaDto) {

        if (cuentaDto.getIdUsuario() == null) {
            throw new IllegalArgumentException("ID de usuario no proporcionado");
        }
        if (cuentaDto.getMonto() == null || cuentaDto.getMonto().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto debe ser mayor o igual a cero");
        }
       if (cuentaRepository.findByNombre(cuentaDto.getNombre()).isPresent()) {
            throw new RuntimeException("El nombre de cuenta " + cuentaDto.getNombre() + " ya existe");
        }
       

        Usuarios usuario = usuariosRepository.findById(cuentaDto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("No se encontró el usuario con ID: " + cuentaDto.getIdUsuario()));

        Cuenta cuenta = new Cuenta();
        cuenta.setNombre(cuentaDto.getNombre());
        cuenta.setMonto(cuentaDto.getMonto());
        cuenta.setUsuarios(usuario);
        cuenta.setMontoLibre(cuentaDto.getMonto());
        
        return cuentaRepository.save(cuenta);
    }

    public List<Cuenta> getAccountByUserId(Long idUsuario) {
        List<Cuenta> cuentas =  cuentaRepository.findByUsuariosIdUsuario(idUsuario);
                return cuentas;
    }

    public Cuenta updCuenta (Long idCuenta, CuentaDto cuentaDto) {
        Cuenta cuenta = cuentaRepository.findById(idCuenta).orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        if (cuentaDto.getMonto() == null || cuentaDto.getMonto().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto debe ser mayor o igual a cero");
        }
       if (cuentaRepository.findByNombre(cuentaDto.getNombre()).isPresent()) {
            throw new RuntimeException("El nombre de cuenta " + cuentaDto.getNombre() + " ya existe");
        }

        cuenta.setNombre(cuentaDto.getNombre());
        cuenta.setMonto(cuentaDto.getMonto());
        return cuentaRepository.save(cuenta);
    }


    public String deleteAccount(Long idCuenta) {
        try {
            cuentaRepository.deleteById(idCuenta);
            return "Cuenta eliminada con éxito";
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la cuenta: " + e.getMessage());
        }
    }
}
