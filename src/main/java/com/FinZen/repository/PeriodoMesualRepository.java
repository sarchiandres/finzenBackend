package com.FinZen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.FinZen.models.Entities.Gastos;
import com.FinZen.models.Entities.Ingresos;
import com.FinZen.models.Entities.PeriodoMesual;

public interface PeriodoMesualRepository extends JpaRepository<PeriodoMesual, Long> {
    @Procedure(name = "getPeriodoMensual")
    List<PeriodoMesual> getPeriodoMensual(@Param("usuarioId") Long usuarioId);

    @Procedure(name = "ObtenerIngresosPorMesYUsuario")
    List<Ingresos>ObtenerIngresosPorMesYUsuario(@Param("anio") int anio,@Param("id_usuario") Long id_usuario);

    @Procedure(name = "ObtenerGastosPorMesYUsuario")
    List<Gastos>ObtenerGastosPorMesYUsuario(@Param("anio") int anio,@Param("id_usuario") Long id_usuario);
}
