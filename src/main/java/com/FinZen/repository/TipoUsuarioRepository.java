package com.FinZen.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FinZen.models.Entities.TipoUsuario;

public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {

    Optional<TipoUsuario> findByNombre(String nombre);
}
