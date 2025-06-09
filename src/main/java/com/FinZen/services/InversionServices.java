package com.FinZen.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinZen.models.DTOS.InversionDto;
import com.FinZen.models.Entities.Inversion;
import com.FinZen.repository.InversionRepository;

@Service
public class InversionServices {

    @Autowired
    private InversionRepository inversionRepository;

    public List<Inversion> getInversionesByUserId(Long idUsuario) {
        return inversionRepository.findByIdUsuario(idUsuario);
    }

    public Inversion createInversion(InversionDto inversionDto) {
        Inversion inversion = new Inversion();
        inversion.setIdUsuario(inversionDto.getIdUsuario());
        inversion.setNombre(inversionDto.getNombre());
        inversion.setTipoInversion(Inversion.TipoInversion.valueOf(inversionDto.getTipoInversion().toUpperCase()));
        inversion.setPlataforma(inversionDto.getPlataforma());
        inversion.setValorInicial(inversionDto.getValorInicial());
        // Si no se proporciona valor actual, usar el valor inicial
        inversion.setValorActual(inversionDto.getValorActual() != null ?
        inversionDto.getValorActual() : inversionDto.getValorInicial());
        inversion.setFechaInicio(inversionDto.getFechaInicio());
        inversion.setFechaCreacion(LocalDateTime.now());
        return inversionRepository.save(inversion);
    }

    public void updateInversion(Long id, InversionDto inversionDto) {
        Inversion inversion = inversionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inversión no encontrada"));
        inversion.setNombre(inversionDto.getNombre());
        inversion.setTipoInversion(Inversion.TipoInversion.valueOf(inversionDto.getTipoInversion().toUpperCase()));
        inversion.setPlataforma(inversionDto.getPlataforma());
        inversion.setValorInicial(inversionDto.getValorInicial());
        inversion.setValorActual(inversionDto.getValorActual());
        inversion.setFechaInicio(inversionDto.getFechaInicio());
        inversionRepository.save(inversion);
    }

    public void deleteInversion(Long id) {
        Inversion inversion = inversionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inversión no encontrada"));
        inversionRepository.delete(inversion);
    }
}