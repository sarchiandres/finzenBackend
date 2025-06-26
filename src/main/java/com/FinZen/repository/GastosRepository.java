package com.FinZen.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.FinZen.models.DTOS.CategoriaGastoConConteoDTO;
import com.FinZen.models.Entities.Gastos;

public interface GastosRepository extends JpaRepository<Gastos, Long> {

    @Query("SELECT g FROM Gastos g WHERE g.presupuesto.cuenta.usuarios.idUsuario = :userId")
    List<Gastos> getGastosByUsuarioId(@Param("userId") Long userId);

    @Query("SELECT new com.FinZen.models.DTOS.CategoriaGastoConConteoDTO(cg.idCategoria, cg.nombre, COUNT(g.idGasto)) " +
           "FROM Gastos g JOIN g.categoria cg JOIN g.presupuesto p JOIN p.cuenta c JOIN c.usuarios u " +
           "WHERE u.idUsuario = :userId " +
           "GROUP BY cg.idCategoria, cg.nombre " +
           "ORDER BY COUNT(g.idGasto) DESC")
    List<CategoriaGastoConConteoDTO> findCategoriasWithExpenseCountsByUsuarioId(@Param("userId") Long userId);

    List<Gastos> findByPresupuestoIdPresupuesto(@Param("presupuestoId") Long presupuestoId);


     @Query("SELECT SUM(g.monto) FROM Gastos g WHERE g.presupuesto.id = :presupuestoId")
    Optional<BigDecimal> sumMontoByPresupuestoId(@Param("presupuestoId") Long presupuestoId);
}