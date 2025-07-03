package com.FinZen.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinZen.models.DTOS.UsuarioDto;
import com.FinZen.models.Entities.TipoUsuario;
import com.FinZen.models.Entities.Usuarios;
import com.FinZen.payload.FinZenException;
import com.FinZen.payload.SignupRequest;
import com.FinZen.repository.TipoUsuarioRepository;
import com.FinZen.repository.UsuariosRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuariosServices {

    @Autowired
    private  UsuariosRepository usuariosRepository;
    @Autowired
    private  S3Service s3Service;
    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    // method to add user

    public Map<String, Object>  saveUsuario (SignupRequest usuarioDto) {

        if (usuariosRepository.findByNombreUsuario(usuarioDto.getNombreUsuario()).isPresent()) {
            throw new RuntimeException("El nombre de usuario " + usuarioDto.getNombreUsuario() + " ya existe");
        }

        if (usuariosRepository.findByNumeroDocumento(usuarioDto.getNumeroDocumento()).isPresent()) {
            throw new RuntimeException("El usuario con documento " + usuarioDto.getNumeroDocumento() + " ya existe");
        }

        if (usuariosRepository.findByCorreo(usuarioDto.getCorreo()).isPresent()) {
            throw new RuntimeException("El usuario con este correo ya existe");
        }
    

        String tipoUsuarioNombre = usuarioDto.getRole() != null ? usuarioDto.getRole().toUpperCase() : "USUARIO";
         if (!Arrays.asList("USUARIO", "ADMINISTRADOR").contains(tipoUsuarioNombre)) {
            throw new FinZenException("Role inválido: " + tipoUsuarioNombre + ". Debe ser USUARIO o ADMINISTRADOR");
        }


        String urlImagen = null;

  
        if (usuarioDto.getUrlImg() != null && !usuarioDto.getUrlImg().isEmpty()) {
            try {
                urlImagen = s3Service.subirArchivo(usuarioDto.getUrlImg());
            } catch (IOException e) {
                throw new RuntimeException("Error al subir la imagen a S3: " + e.getMessage(), e);
            }
        }


        
        

        Usuarios usuario = new Usuarios();
        usuario.setNombre(usuarioDto.getNombre());
        usuario.setCorreo(usuarioDto.getCorreo());
        usuario.setContrasena(passwordEncoder.encode(usuarioDto.getContrasena()));
        usuario.setNumeroDocumento(usuarioDto.getNumeroDocumento());
        usuario.setPaisResidencia(usuarioDto.getPaisResidencia());
        usuario.setIngresoMensual(usuarioDto.getIngresoMensual());
        usuario.setMetaActual(usuarioDto.getMetaActual());
        usuario.setNombreUsuario(usuarioDto.getNombreUsuario());
        usuario.setTipoDocumento(usuarioDto.getTipoDocumento());
        usuario.setTipoPersona(usuarioDto.getTipoPersona());
        usuario.setUrlImg(urlImagen); 

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findByNombre(tipoUsuarioNombre)
                .orElseThrow(() -> new FinZenException("El tipo de usuario '" + tipoUsuarioNombre + "' no existe"));
        usuario.setTipoUsuario(tipoUsuario);

        Usuarios savedUsuario = usuariosRepository.save(usuario);
        
         Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", savedUsuario.getIdUsuario());
        responseData.put("nombre", savedUsuario.getNombre());
        responseData.put("correo", savedUsuario.getCorreo());
        responseData.put("nombreUsuario", savedUsuario.getNombreUsuario());
        responseData.put("tipoUsuarioId", savedUsuario.getTipoUsuario().getIdTipoUsuario());
        responseData.put("tipoUsuarioNombre", savedUsuario.getTipoUsuario().getNombre());

       
        return responseData;
    }


    // method to find user by id

    public Usuarios finById(Long id) {
        return usuariosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }


    // method to delete user by id
    public String deleteById(Long id) {
        try {
            usuariosRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                    
                    usuariosRepository.deleteById(id);
                    return "Usuario eliminado correctamente";

                    
        } catch (RuntimeException e) {
            return e.getMessage();
        }
        
    }




    // method to update user by id
    public Usuarios updateUsuario(Long id, UsuarioDto usuarioDto) {
        
        Usuarios usuario = usuariosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuarioDto.getNombre() != null) {
            usuario.setNombre(usuarioDto.getNombre());
        }
        if (usuarioDto.getCorreo() != null) {
            usuario.setCorreo(usuarioDto.getCorreo());
        }
        if (usuarioDto.getContrasena() != null) {
            usuario.setContrasena(usuarioDto.getContrasena());
        }
        if (usuarioDto.getNumeroDocumento() != null) {
            usuario.setNumeroDocumento(usuarioDto.getNumeroDocumento());
        }
        if (usuarioDto.getPaisResidencia() != null) {
            usuario.setPaisResidencia(usuarioDto.getPaisResidencia());
        }
        if (usuarioDto.getIngresoMensual() != null) {
            usuario.setIngresoMensual(usuarioDto.getIngresoMensual());
        }
        if (usuarioDto.getMetaActual() != null) {
            usuario.setMetaActual(usuarioDto.getMetaActual());
        }
        if (usuarioDto.getNombreUsuario() != null) {
            usuario.setNombreUsuario(usuarioDto.getNombreUsuario());
        }
        if (usuarioDto.getTipoDocumento() != null) {
            usuario.setTipoDocumento(usuarioDto.getTipoDocumento());
        }
        if (usuarioDto.getTipoPersona() != null) {
            usuario.setTipoPersona(usuarioDto.getTipoPersona());

        }
        if (usuarioDto.getTipoPersona() != null) {
            usuario.setTipoPersona(usuarioDto.getTipoPersona());
        }

        String urlImagen = null;

  
        if (usuarioDto.getUrlImg() != null && !usuarioDto.getUrlImg().isEmpty()) {
            try {
                urlImagen = s3Service.subirArchivo(usuarioDto.getUrlImg());
                usuario.setUrlImg(urlImagen);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir la imagen a S3: " + e.getMessage(), e);
            }
        }
        return usuariosRepository.save(usuario);
        }




/*Funtions this admi */

    public List<Usuarios> findAll() {
        return usuariosRepository.findAll();
    }



    public String deleteUsuarioByOtroUsuario(Long idUsuarioSolicitante, Long idUsuarioAEliminar) {
       
        Usuarios usuarioSolicitante = usuariosRepository.findById(idUsuarioSolicitante)
                .orElseThrow(() -> new RuntimeException("Usuario solicitante no encontrado"));

      
        Usuarios usuarioAEliminar = usuariosRepository.findById(idUsuarioAEliminar)
                .orElseThrow(() -> new RuntimeException("Usuario a eliminar no encontrado"));

        
        if (usuarioSolicitante.getTipoUsuario().getNombre().equals("ADMINISTRADOR")) { 
            throw new RuntimeException("El usuario solicitante no tiene permisos para eliminar a otro usuario");
        }

        // Eliminar el usuario
        usuariosRepository.deleteById(idUsuarioAEliminar);

        return "Usuario con ID " + idUsuarioAEliminar + " eliminado correctamente ";
    }  


     public boolean cambiarContrasena(Long id , String currentPassword, String newPassword) {
        //  Encontrar al usuario por su correo (o su nombre de usuario, según tu lógica de autenticación)
        Usuarios usuario = usuariosRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + id));

        // Verificar que la contraseña actual proporcionada sea correcta
        if (!passwordEncoder.matches(currentPassword, usuario.getContrasena())) {
            return false; // Contraseña actual incorrecta
        }

        //  Cifrar la nueva contraseña
        String hashedNewPassword = passwordEncoder.encode(newPassword);

        // Actualizar la contraseña del usuario
        usuario.setContrasena(hashedNewPassword);
        usuariosRepository.save(usuario);
        return true; // Contraseña cambiada exitosamente
    }
}
