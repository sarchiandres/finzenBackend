package com.FinZen.security.Jwt;

import com.FinZen.models.Entities.Usuarios;
import com.FinZen.repository.UsuariosRepository; // Asegúrate que la ruta de tu repositorio es correcta

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importa esta anotación si no la tienes

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuariosRepository usuarioRepository;

    // Constructor para inyección de dependencias
    public UserDetailsServiceImpl(UsuariosRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional // ¡MUY IMPORTANTE! Asegura la sesión para cargar relaciones perezosas.
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuarios usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + correo));

        // ***** BLOQUE CRÍTICO DE DEPURACIÓN Y MANEJO DE NULOS *****

        // 1. Verificar si getTipoUsuario() es null
        if (usuario.getTipoUsuario() == null) {
            System.err.println("--- DEBUG FinZen --- ERROR CRÍTICO: usuario.getTipoUsuario() es NULL para el correo: " + correo);
            // Lanza una excepción más clara para ti en el backend
            throw new IllegalStateException("Error de datos del usuario: El TipoUsuario está NULL para el correo " + correo);
        }

        // 2. Obtener el nombre del TipoUsuario
        String tipoUsuarioNombre = usuario.getTipoUsuario().getNombre();

        // 3. Verificar si el nombre es null o vacío
        if (tipoUsuarioNombre == null) {
            System.err.println("--- DEBUG FinZen --- ERROR CRÍTICO: El nombre del TipoUsuario es NULL para el correo: " + correo + " (TipoUsuario ID: " + usuario.getTipoUsuario().getIdTipoUsuario() + ")");
            throw new IllegalStateException("Error de datos del usuario: El nombre del TipoUsuario es NULL para el correo " + correo);
        }
        if (tipoUsuarioNombre.trim().isEmpty()) {
            System.err.println("--- DEBUG FinZen --- ERROR CRÍTICO: El nombre del TipoUsuario es una cadena vacía o solo espacios para el correo: " + correo + " (TipoUsuario ID: " + usuario.getTipoUsuario().getIdTipoUsuario() + ")");
            throw new IllegalStateException("Error de datos del usuario: El nombre del TipoUsuario está vacío para el correo " + correo);
        }
        // ***** FIN BLOQUE CRÍTICO *****

        // Línea 29 modificada para usar la variable 'tipoUsuarioNombre' ya validada
        String role = "ROLE_" + tipoUsuarioNombre.toUpperCase();
        System.out.println("--- DEBUG FinZen --- Rol construido para " + correo + ": " + role); // Para ver el rol final

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

        // Asumiendo que CustomUserDetails tiene este constructor
        return new CustomUserDetails(usuario.getIdUsuario(),
                                     usuario.getCorreo(),
                                     usuario.getContrasena(),
                                     List.of(authority));
    }
}