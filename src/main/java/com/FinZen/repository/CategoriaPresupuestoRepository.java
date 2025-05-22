package com.FinZen.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.FinZen.models.Entities.CategoriaPresupuesto;

public interface CategoriaPresupuestoRepository  extends JpaRepository<CategoriaPresupuesto, Long> {
    Optional<CategoriaPresupuesto> findByNombre(String nombre);

    
}
