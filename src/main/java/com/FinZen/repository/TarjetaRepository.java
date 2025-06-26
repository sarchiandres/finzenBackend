package com.FinZen.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.FinZen.models.Entities.Tarjeta;

public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {
    List<Tarjeta> findByIdUsuario(Long idUsuario);
}