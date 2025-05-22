package com.FinZen.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinZen.models.Entities.Gastos;
import com.FinZen.models.Entities.Ingresos;
import com.FinZen.models.Entities.PeriodoMesual;
import com.FinZen.repository.PeriodoMesualRepository;

import jakarta.transaction.Transactional;

@Service
public class PeridoMensualServices {
    @Autowired
    private PeriodoMesualRepository periodoMesualRepository;
    
    @Transactional
    public List<PeriodoMesual> getPeriodoMensual(Long usuarioId) {
        return periodoMesualRepository.getPeriodoMensual(usuarioId);
    }

   @Transactional
    public List<Gastos> obGastosPorMes( int anio ,Long id_usuario) {
        return periodoMesualRepository.ObtenerGastosPorMesYUsuario(anio,id_usuario);

    }
    
    @Transactional
    public List<Ingresos> obIngresosPorMes( int anio,Long id_usuario) {
        return periodoMesualRepository.ObtenerIngresosPorMesYUsuario(anio,id_usuario);
    }
}
