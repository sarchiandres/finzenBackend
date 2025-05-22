package com.FinZen.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.FinZen.models.Entities.Meta;

public  interface MetaRepository  extends JpaRepository<Meta, Long> {
   
    Optional<Meta> findByTitulo(String nombre);
    List<Meta> findByCuentaIdCuenta(Long idCuenta);

    @Query("SELECT m FROM Meta m WHERE m.cuenta.usuarios.idUsuario = :idUsuario")
    List<Meta> findByUsuarioId(@Param("idUsuario") Long idUsuario);
    
}
