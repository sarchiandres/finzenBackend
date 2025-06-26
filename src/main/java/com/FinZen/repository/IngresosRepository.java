package com.FinZen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.FinZen.models.Entities.Ingresos;

public interface IngresosRepository  extends JpaRepository<Ingresos, Long> {

    @Query("SELECT i FROM Ingresos i WHERE i.cuenta.usuarios.idUsuario= :userId")
    List<Ingresos> findByUsuarioId(@Param("userId") Long userId);
    

    
}
