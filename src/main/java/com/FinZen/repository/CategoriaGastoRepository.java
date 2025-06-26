package com.FinZen.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.FinZen.models.Entities.CategoriaGasto;



public interface CategoriaGastoRepository extends JpaRepository<CategoriaGasto, Long> {
   Optional<CategoriaGasto> findByNombre(String nombre);


}
