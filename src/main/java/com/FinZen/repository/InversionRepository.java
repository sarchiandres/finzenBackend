package com.FinZen.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.FinZen.models.Entities.Inversion;

public interface InversionRepository extends JpaRepository<Inversion, Long> {
    List<Inversion> findByIdUsuario(Long idUsuario);
}