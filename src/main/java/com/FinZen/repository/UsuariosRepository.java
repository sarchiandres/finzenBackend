

package com.FinZen.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.FinZen.models.Entities.Usuarios;


public interface UsuariosRepository  extends JpaRepository<Usuarios, Long> {

    Optional<Usuarios> findByNumeroDocumento(Long numeroDocumento);
    Optional<Usuarios> findByNombreUsuario(String nombreUsuario);
    Optional<Usuarios> findByCorreo(String correo);
    
}