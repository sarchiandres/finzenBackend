package com.FinZen.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.FinZen.models.Entities.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    List<Cuenta> findByUsuariosIdUsuario(Long idUsuario);
    Optional<Cuenta> findByNombre(String nombre);
}
