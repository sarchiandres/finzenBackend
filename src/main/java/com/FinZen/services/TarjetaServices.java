package com.FinZen.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinZen.models.DTOS.TarjetaDto;
import com.FinZen.models.Entities.Tarjeta;
import com.FinZen.repository.TarjetaRepository;

@Service
public class TarjetaServices {

    @Autowired
    private TarjetaRepository tarjetaRepository;

    public List<Tarjeta> getTarjetasByUserId(Long idUsuario) {
        return tarjetaRepository.findByIdUsuario(idUsuario);
    }

    public Tarjeta createTarjeta(TarjetaDto tarjetaDto) {
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setIdUsuario(tarjetaDto.getIdUsuario());
        tarjeta.setNombre(tarjetaDto.getNombre());
        tarjeta.setTipo(Tarjeta.TipoTarjeta.valueOf(tarjetaDto.getTipo().toUpperCase()));
        tarjeta.setBanco(tarjetaDto.getBanco());
        tarjeta.setNumeroUltimosDigitos(tarjetaDto.getNumeroUltimosDigitos());
        tarjeta.setSaldoActual(tarjetaDto.getSaldoActual());
        // Para tarjetas de crédito, límite de crédito es requerido
        if (tarjeta.getTipo() == Tarjeta.TipoTarjeta.CREDITO && tarjetaDto.getLimiteCredito() == null) {
            throw new IllegalArgumentException("Las tarjetas de crédito requieren límite de crédito");
        }
        tarjeta.setLimiteCredito(tarjetaDto.getLimiteCredito());
        tarjeta.setFechaCreacion(LocalDateTime.now());
        return tarjetaRepository.save(tarjeta);
    }

    public void updateTarjeta(Long id, TarjetaDto tarjetaDto) {
        Tarjeta tarjeta = tarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
        tarjeta.setNombre(tarjetaDto.getNombre());
        tarjeta.setTipo(Tarjeta.TipoTarjeta.valueOf(tarjetaDto.getTipo().toUpperCase()));
        tarjeta.setBanco(tarjetaDto.getBanco());
        tarjeta.setNumeroUltimosDigitos(tarjetaDto.getNumeroUltimosDigitos());
        tarjeta.setSaldoActual(tarjetaDto.getSaldoActual());

        // Validar límite de crédito para tarjetas de crédito
        if (tarjeta.getTipo() == Tarjeta.TipoTarjeta.CREDITO && tarjetaDto.getLimiteCredito() == null) {
            throw new IllegalArgumentException("Las tarjetas de crédito requieren límite de crédito");
        }
        tarjeta.setLimiteCredito(tarjetaDto.getLimiteCredito());
        tarjetaRepository.save(tarjeta);
    }

    public void deleteTarjeta(Long id) {
        Tarjeta tarjeta = tarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
        tarjetaRepository.delete(tarjeta);
    }
}