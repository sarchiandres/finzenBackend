package com.FinZen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.models.DTOS.UsuarioDto;
import com.FinZen.services.UsuariosServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/finzen/usuarios")
public class UsuariosControllers {
    @Autowired
    private UsuariosServices usuariosServices;

// para crear el usuario
    @PostMapping
    public ResponseEntity<?> saveUsuario(@RequestBody UsuarioDto usuarioDto) {
        try {
            usuariosServices.saveUsuario(usuarioDto);
            return ResponseEntity.ok("Usuario creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear el usuario: " + e.getMessage());
        }
    }

    // metodo para traer el usuario por id
    @GetMapping("{id}")
    public ResponseEntity<?> getUsuarioById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(usuariosServices.finById(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener el usuario: " + e.getMessage());
        }
    }
// para elimnar el usuario
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id) {
        try {
            usuariosServices.deleteById(id);
            return ResponseEntity.ok("Usuario eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar el usuario: " + e.getMessage());
        }
    }
// para actualizar el usuario
    @PutMapping("{id}")
    public String putMethodName(@PathVariable Long id, @RequestBody UsuarioDto usuarioDto) {
        try {
            usuariosServices.updateUsuario(id, usuarioDto);
            return "Usuario actualizado exitosamente";
        } catch (Exception e) {
            return "Error al actualizar el usuario: " + e.getMessage();
        }
    }
}
