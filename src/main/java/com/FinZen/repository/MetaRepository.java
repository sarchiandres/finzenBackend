package com.FinZen.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.FinZen.models.Entities.Meta;

public interface MetaRepository extends JpaRepository<Meta, Long> {

    @Query("SELECT m FROM Meta m WHERE m.usuario.idUsuario = :idUsuario")
    List<Meta> findMetasByUsuarioId(@Param("idUsuario") Long idUsuario);

    Optional<Meta> findByTitulo(String nombre);

    @Query("SELECT m FROM Meta m WHERE m.usuario.idUsuario = :idUsuario")
    List<Meta> findByUsuarioId(@Param("idUsuario") Long idUsuario);

    @Query("SELECT m FROM Meta m WHERE m.usuario.idUsuario = :idUsuario AND m.fechaLimite <= :fecha")
    List<Meta> findMetasProximas(@Param("idUsuario") Long idUsuario, @Param("fecha") LocalDate fechaLimite);
}