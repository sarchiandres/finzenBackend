package com.FinZen.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinZen.models.DTOS.MetaDto;
import com.FinZen.models.Entities.Cuenta;
import com.FinZen.models.Entities.Meta;
import com.FinZen.repository.CuentaRepository;
import com.FinZen.repository.MetaRepository;

@Service
public class MetaServices {
    @Autowired
    private MetaRepository metaRepository;
    @Autowired
    private CuentaRepository cuentaRepository;

    public Meta createMeta(MetaDto metaDto) {
        if (metaRepository.findByTitulo(metaDto.getTitulo()).isPresent()) {
            throw new RuntimeException("La meta " + metaDto.getTitulo() + " ya existe");
        }

        Cuenta cuenta = cuentaRepository.findById(metaDto.getIdCuenta())
                .orElseThrow(() -> new RuntimeException("La cuenta no existe"));

        Meta meta = new Meta();
        meta.setTitulo(metaDto.getTitulo());
        meta.setValor(metaDto.getValor());
        meta.setCuenta(cuenta);
        meta.setDescripcion(metaDto.getDescripcion());
        meta.setEnProgreso(metaDto.getEnProgreso());
        meta.setEstado(metaDto.getEstado());
        meta.setFechaInicio(metaDto.getFechaInicio());
        meta.setFechaLimite(metaDto.getFechaLimite());
        
        return metaRepository.save(meta);
    }

    public Meta updateMeta(Long idMeta, MetaDto metaDto) {
        Meta existingMeta = metaRepository.findById(idMeta)
                .orElseThrow(() -> new RuntimeException("No se encontró la meta con ID: " + idMeta));

        Cuenta cuenta = cuentaRepository.findById(metaDto.getIdCuenta())
                .orElseThrow(() -> new RuntimeException("La cuenta no existe"));

        if (metaRepository.findByTitulo(metaDto.getTitulo()).isPresent()) {
               throw new RuntimeException("La meta " + metaDto.getTitulo() + " ya existe");
        }

        existingMeta.setTitulo(metaDto.getTitulo());
        existingMeta.setValor(metaDto.getValor());
        existingMeta.setCuenta(cuenta);
        existingMeta.setDescripcion(metaDto.getDescripcion());
        existingMeta.setEnProgreso(metaDto.getEnProgreso());
        existingMeta.setEstado(metaDto.getEstado());
        existingMeta.setFechaInicio(metaDto.getFechaInicio());
        existingMeta.setFechaLimite(metaDto.getFechaLimite());

        return metaRepository.save(existingMeta);
    }



    public List<Meta> getAllMetas(Long idCuenta) {
        List<Meta> metas = metaRepository.findByCuentaIdCuenta(idCuenta);
        return metas;
    }

    public String deleteMeta(Long idMeta) {
        Meta meta = metaRepository.findById(idMeta)
                .orElseThrow(() -> new RuntimeException("No se encontró la meta con ID: " + idMeta));

        metaRepository.delete(meta);
        return "Meta eliminada con éxito";
    }
}
