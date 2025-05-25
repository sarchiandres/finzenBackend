package com.FinZen.security.Jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.FinZen.models.Entities.Usuarios;
import com.FinZen.repository.UsuariosRepository;

import java.util.Collections;
import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuariosRepository usuarioRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuarios usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + correo));

        String role = "ROLE_" + usuario.getTipoUsuario().getNombre().toUpperCase();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

        // Retornar un objeto CustomUserDetails con el ID del usuario
        return new CustomUserDetails(
                usuario.getIdUsuario(), // ID del usuario
                usuario.getCorreo(), // Nombre de usuario
                usuario.getContrasena(), // Contraseña
                Collections.singletonList(authority) // Roles/autoridades
        );
    }
}
