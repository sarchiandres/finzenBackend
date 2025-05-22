package com.FinZen.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FinZen.models.Entities.Soporte;

public interface SoporteRepository extends JpaRepository<Soporte, Long> {

    Optional<Soporte> findByPregunta(String nombre);
}
