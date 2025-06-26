package com.FinZen.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.FinZen.models.Entities.Informe;

@Repository
public interface InformeRepository extends JpaRepository<Informe, Long> {

    /**
     * Encuentra todos los informes de un usuario ordenados por ID descendente (más reciente primero)
     */
    @Query("SELECT i FROM Informe i WHERE i.usuario.idUsuario = :usuarioId ORDER BY i.idInforme DESC")
    List<Informe> findByUsuarioIdOrderByIdInformeDesc(@Param("usuarioId") Long usuarioId);

    /**
     * Encuentra todos los informes de un usuario
     */
    @Query("SELECT i FROM Informe i WHERE i.usuario.idUsuario = :usuarioId")
    List<Informe> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Encuentra un informe específico por ID y usuario
     */
    @Query("SELECT i FROM Informe i WHERE i.idInforme = :informeId AND i.usuario.idUsuario = :usuarioId")
    Optional<Informe> findByIdInformeAndUsuarioId(@Param("informeId") Long informeId, @Param("usuarioId") Long usuarioId);

    /**
     * Cuenta el número de informes de un usuario
     */
    @Query("SELECT COUNT(i) FROM Informe i WHERE i.usuario.idUsuario = :usuarioId")
    Long countByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Elimina todos los informes de un usuario
     */
    @Query("DELETE FROM Informe i WHERE i.usuario.idUsuario = :usuarioId")
    void deleteByUsuarioId(@Param("usuarioId") Long usuarioId);
}