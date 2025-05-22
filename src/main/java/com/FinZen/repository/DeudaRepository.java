package com.FinZen.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.FinZen.models.Entities.Deuda;

public interface DeudaRepository extends JpaRepository<Deuda, Long> {

    List<Deuda> findByCuentaIdCuenta(Long idCuenta);

    Optional<Deuda> findByNombre(String nombre);


    @Query("SELECT d FROM Deuda d WHERE d.cuenta.usuarios.idUsuario = :idUsuario")
    List<Deuda> findByIdUsuario(@Param("idUsuario")Long idUsuario);
}
