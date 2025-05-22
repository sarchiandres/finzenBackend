package com.FinZen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.FinZen.models.Entities.Presupuesto;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {
    @Procedure(procedureName = "GetPresupuestosByCuentaId")
    List<Presupuesto> findByCuentaIdCuenta( @Param("cuentaId") Long cuentaId);
    
   @Query("SELECT p FROM Presupuesto p WHERE p.cuenta.usuarios.idUsuario = :idUsuario")
List<Presupuesto> findByUsuarioId(@Param("idUsuario") Long idUsuario);
    
}
