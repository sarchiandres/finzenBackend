package com.FinZen.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinZen.models.DTOS.PresupuestoDto;
import com.FinZen.models.DTOS.PresupuestoResponseDto;
import com.FinZen.models.Entities.CategoriaPresupuesto;
import com.FinZen.models.Entities.Cuenta;
import com.FinZen.models.Entities.Inversion;
import com.FinZen.models.Entities.Presupuesto;
import com.FinZen.models.Entities.Tarjeta;
import com.FinZen.repository.CategoriaPresupuestoRepository;
import com.FinZen.repository.CuentaRepository;
import com.FinZen.repository.GastosRepository;
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
    @Autowired
    private GastosRepository gastosRepository;

    @Transactional
    public Presupuesto savePresupuesto(PresupuestoDto presupuestoDto) {
        // Validar que solo una de las asociaciones (cuenta, tarjeta, inversion) esté presente
        int associationsCount = 0;
        if (presupuestoDto.getIdCuenta() != null && presupuestoDto.getIdCuenta() > 0) associationsCount++;
        if (presupuestoDto.getIdTarjeta() != null && presupuestoDto.getIdTarjeta() > 0) associationsCount++;
        if (presupuestoDto.getIdInversion() != null && presupuestoDto.getIdInversion() > 0) associationsCount++;

        if (associationsCount == 0) {
            throw new IllegalArgumentException("Un presupuesto debe estar asociado a una cuenta, una tarjeta o una inversión.");
        }
        if (associationsCount > 1) {
            throw new IllegalArgumentException("Un presupuesto solo puede estar asociado a una única entidad (cuenta, tarjeta o inversión).");
        }

        Cuenta miCuenta = null;
        Tarjeta miTarjeta = null;
        Inversion mInversion = null;

        if (presupuestoDto.getIdCuenta() != null && presupuestoDto.getIdCuenta() > 0) {
            miCuenta = cuentaRepository.findById(presupuestoDto.getIdCuenta())
                .orElseThrow(() -> new RuntimeException("La cuenta con ID " + presupuestoDto.getIdCuenta() + " no se encontró."));
        } else if (presupuestoDto.getIdTarjeta() != null && presupuestoDto.getIdTarjeta() > 0) {
            miTarjeta = tarjetaRepository.findById(presupuestoDto.getIdTarjeta())
                .orElseThrow(() -> new RuntimeException("La tarjeta con ID " + presupuestoDto.getIdTarjeta() + " no se encontró."));
        } else if (presupuestoDto.getIdInversion() != null && presupuestoDto.getIdInversion() > 0) {
            mInversion = inversionRepository.findById(presupuestoDto.getIdInversion())
                .orElseThrow(() -> new RuntimeException("La inversión con ID " + presupuestoDto.getIdInversion() + " no se encontró."));
        }

        CategoriaPresupuesto categoria = categoriaPresupuestoRepository.findById(presupuestoDto.getIdCategoriaPresupuesto())
                    .orElseThrow(() -> new RuntimeException("La categoría de presupuesto con ID " + presupuestoDto.getIdCategoriaPresupuesto() + " no se ha encontrado."));

        Presupuesto miPresupuesto = new Presupuesto();
        miPresupuesto.setNombre(presupuestoDto.getNombre());
        miPresupuesto.setMontoAsignado(presupuestoDto.getMontoAsignado());
        miPresupuesto.setCategoria(categoria);
        miPresupuesto.setCuenta(miCuenta); // Se establecerá a null si no se proporcionó idCuenta
        miPresupuesto.setTarjeta(miTarjeta); // Se establecerá a null si no se proporcionó idTarjeta
        miPresupuesto.setInversion(mInversion); // Se establecerá a null si no se proporcionó idInversion
        
        return presupuestoRepository.save(miPresupuesto);
    }

    @Transactional
    public Presupuesto updatePresupuesto(Long idPresupuesto, PresupuestoDto presupuestoDto) {
        Presupuesto miPresupuesto = presupuestoRepository.findById(idPresupuesto)
                .orElseThrow(() -> new RuntimeException("El presupuesto con ID " + idPresupuesto + " no se encontró."));

        // Validar que solo una de las asociaciones (cuenta, tarjeta, inversion) esté presente en el DTO
        int associationsCount = 0;
        if (presupuestoDto.getIdCuenta() != null && presupuestoDto.getIdCuenta() > 0) associationsCount++;
        if (presupuestoDto.getIdTarjeta() != null && presupuestoDto.getIdTarjeta() > 0) associationsCount++;
        if (presupuestoDto.getIdInversion() != null && presupuestoDto.getIdInversion() > 0) associationsCount++;

        if (associationsCount == 0) {
            throw new IllegalArgumentException("Un presupuesto debe estar asociado a una cuenta, una tarjeta o una inversión.");
        }
        if (associationsCount > 1) {
            throw new IllegalArgumentException("Un presupuesto solo puede estar asociado a una única entidad (cuenta, tarjeta o inversión).");
        }

        // Resetear las asociaciones existentes antes de establecer las nuevas para asegurar exclusividad
        miPresupuesto.setCuenta(null);
        miPresupuesto.setTarjeta(null);
        miPresupuesto.setInversion(null);

        // Establecer la nueva asociación basada en el DTO
        if (presupuestoDto.getIdCuenta() != null && presupuestoDto.getIdCuenta() > 0) {
            Cuenta miCuenta = cuentaRepository.findById(presupuestoDto.getIdCuenta())
                .orElseThrow(() -> new RuntimeException("La cuenta con ID " + presupuestoDto.getIdCuenta() + " no se encontró."));
            miPresupuesto.setCuenta(miCuenta);
        } else if (presupuestoDto.getIdTarjeta() != null && presupuestoDto.getIdTarjeta() > 0) {
            Tarjeta miTarjeta = tarjetaRepository.findById(presupuestoDto.getIdTarjeta())
                .orElseThrow(() -> new RuntimeException("La tarjeta con ID " + presupuestoDto.getIdTarjeta() + " no se encontró."));
            miPresupuesto.setTarjeta(miTarjeta);
        } else if (presupuestoDto.getIdInversion() != null && presupuestoDto.getIdInversion() > 0) {
            Inversion mInversion = inversionRepository.findById(presupuestoDto.getIdInversion())
                .orElseThrow(() -> new RuntimeException("La inversión con ID " + presupuestoDto.getIdInversion() + " no se encontró."));
            miPresupuesto.setInversion(mInversion);
        }
        
        CategoriaPresupuesto categoria = categoriaPresupuestoRepository.findById(presupuestoDto.getIdCategoriaPresupuesto())
                .orElseThrow(() -> new RuntimeException("La categoría de presupuesto con ID " + presupuestoDto.getIdCategoriaPresupuesto() + " no se ha encontrado."));

        miPresupuesto.setCategoria(categoria);
        miPresupuesto.setNombre(presupuestoDto.getNombre());
        miPresupuesto.setMontoAsignado(presupuestoDto.getMontoAsignado());

        return presupuestoRepository.save(miPresupuesto);
    }

    @Transactional
    public List<PresupuestoResponseDto> getPresupuestosForUser(Long userId) {
        List<Presupuesto> presupuestos = presupuestoRepository.findByUsuarioId(userId);
      
        return presupuestos.stream()
            .map(presupuesto -> {
                BigDecimal totalGastado = gastosRepository.sumMontoByPresupuestoId(presupuesto.getIdPresupuesto())
                        .orElse(BigDecimal.ZERO); 

                return new PresupuestoResponseDto(
                    presupuesto.getIdPresupuesto(),
                    presupuesto.getNombre(),
                    presupuesto.getMontoAsignado(),
                    presupuesto.getCuenta(),
                    presupuesto.getTarjeta(),
                    presupuesto.getInversion(),
                    presupuesto.getCategoria(),
                    totalGastado 
                );
            })
            .collect(Collectors.toList()); 
    }

    @Transactional
    public PresupuestoResponseDto getPresupuestoByIdWithGastos(Long idPresupuesto) {
        Presupuesto presupuesto = presupuestoRepository.findById(idPresupuesto)
            .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado con ID: " + idPresupuesto));

        BigDecimal totalGastado = gastosRepository.sumMontoByPresupuestoId(presupuesto.getIdPresupuesto())
                                                .orElse(BigDecimal.ZERO);

        return new PresupuestoResponseDto(
            presupuesto.getIdPresupuesto(),
            presupuesto.getNombre(),
            presupuesto.getMontoAsignado(),
            presupuesto.getCuenta(),
            presupuesto.getTarjeta(),
            presupuesto.getInversion(),
            presupuesto.getCategoria(),
            totalGastado
        );
    }

    @Transactional
    public String deletePresupuesto(Long idPresupuesto) {
        if (!presupuestoRepository.existsById(idPresupuesto)) {
            throw new RuntimeException("El presupuesto con ID " + idPresupuesto + " no existe.");
        }
        presupuestoRepository.deleteById(idPresupuesto);
        return "Presupuesto eliminado con éxito.";
    }

    // Métodos para obtener entidades Presupuesto directamente (usados por el controlador para rutas específicas)
    public List<Presupuesto> getPresupuestosByCuenta(Long idCuenta) {
        return presupuestoRepository.findByCuentaIdCuenta(idCuenta);
    }

    public List<Presupuesto> getPresupuestosByTarjeta(Long idTarjeta) {
        return presupuestoRepository.findByTarjetaIdTarjeta(idTarjeta);
    }

    public List<Presupuesto> getPresupuestosByInversion(Long idInversion) {
        return presupuestoRepository.findByInversionIdInversion(idInversion);
    }
}