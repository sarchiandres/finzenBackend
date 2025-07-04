package com.FinZen.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.FinZen.models.Entities.Usuarios;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {

    Optional<Usuarios> findByNumeroDocumento(Long numeroDocumento);
    Optional<Usuarios> findByNombreUsuario(String nombreUsuario);
    Optional<Usuarios> findByCorreo(String correo);

    @Query("SELECT u FROM Usuarios u WHERE u.numeroDocumento = :numeroDocumento")
    boolean existsByNumeroDocumento(@Param("numeroDocumento") Long numeroDocumento);

    @Query("SELECT COUNT(u) > 0 FROM Usuarios u WHERE u.correo = :correo")
    boolean existsByCorreo(@Param("correo") String correo);

    @Query("SELECT COUNT(u) > 0 FROM Usuarios u WHERE u.nombreUsuario = :nombreUsuario")
    boolean existsByNombreUsuario(@Param("nombreUsuario") String nombreUsuario);
}