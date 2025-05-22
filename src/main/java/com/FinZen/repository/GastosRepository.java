package com.FinZen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.FinZen.models.Entities.Gastos;

public interface GastosRepository extends JpaRepository<Gastos, Long> {

    @Query("SELECT g FROM Gastos g WHERE g.presupuesto.cuenta.usuarios.idUsuario = :userId")
List<Gastos> getGastosByUsuarioId(@Param("userId") Long userId);

 
    List<Gastos> findByPresupuestoIdPresupuesto(@Param("presupuestoId") Long presupuestoId);
}
    


