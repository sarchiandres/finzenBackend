package com.FinZen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.models.DTOS.UsuarioDto;
import com.FinZen.models.Entities.Usuarios;
import com.FinZen.payload.SignupRequest;
import com.FinZen.security.Jwt.JwtUtils;
import com.FinZen.services.UsuariosServices;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/finzen/usuarios")
public class UsuariosControllers {
    @Autowired
    private UsuariosServices usuariosServices;
    @Autowired
    private JwtUtils jwtUtils;

    public String token;

// para crear el usuario
    @PostMapping
    public ResponseEntity<?> saveUsuario(@RequestBody SignupRequest usuarioDto) {
        try {
            usuariosServices.saveUsuario(usuarioDto);
            return ResponseEntity.ok("Usuario creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear el usuario: " + e.getMessage());
        }
    }

    // metodo para traer el usuario por id
    @GetMapping
public ResponseEntity<?> getPerfil(HttpServletRequest request) {
     token = jwtUtils.getJwtFromRequest(request);

    if (token != null && jwtUtils.validateJwtToken(token)) {
        Long userId = jwtUtils.getUserIdFromJwtToken(token);

        // Buscar al usuario en la base de datos
        Usuarios usuario = usuariosServices.finById(userId);
                
        return ResponseEntity.ok(usuario);
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
}

    
// para elimnar el usuario
    @DeleteMapping
    public ResponseEntity<?> deleteUsuario(HttpServletRequest request) {
         token= jwtUtils.getJwtFromRequest(request);

    if (token != null && jwtUtils.validateJwtToken(token)) {
        Long userId = jwtUtils.getUserIdFromJwtToken(token);
        // Buscar al usuario en la base de datos
        usuariosServices.deleteById(userId);
        return ResponseEntity.ok("Usuario eliminado exitosamente");
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }
// para actualizar el usuario
    @PutMapping
    public ResponseEntity<?> putMethodName(HttpServletRequest request, @RequestBody UsuarioDto usuarioDto) {
        token= jwtUtils.getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            // Buscar al usuario en la base de datos
           Usuarios usuario= usuariosServices.updateUsuario(userId, usuarioDto);
            return ResponseEntity.ok(usuario); 
        }
    
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }
}
