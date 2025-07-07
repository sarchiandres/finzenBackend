package com.FinZen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.FinZen.models.Entities.Ingresos;

public interface IngresosRepository  extends JpaRepository<Ingresos, Long> {

    @Query("SELECT i FROM Ingresos i WHERE i.cuenta.usuarios.idUsuario= :userId")
    List<Ingresos> findByUsuarioId(@Param("userId") Long userId);
    
    @Query("SELECT SUM(i.monto) FROM Ingresos i WHERE i.cuenta.usuarios.idUsuario = :userId")
    Double sumIngresosByUserId(@Param("userId")long userId);
    

    @Query("SELECT i FROM Ingresos i WHERE i.cuenta.usuarios.idUsuario = :userId AND i.fecha BETWEEN :startDate AND :endDate")
    List<Ingresos> findByUsuarioIdAndFechaBetween(@Param("userId") Long userId, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
