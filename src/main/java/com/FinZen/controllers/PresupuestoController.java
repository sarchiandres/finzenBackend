package com.FinZen.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.models.DTOS.PresupuestoDto;
import com.FinZen.models.Entities.Presupuesto;
import com.FinZen.models.Entities.Usuarios;
import com.FinZen.repository.PresupuestoRepository;
import com.FinZen.security.Jwt.JwtUtils;
import com.FinZen.services.PresupuestoServices;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/finzen/presupuesto")
public class PresupuestoController {
    
    @Autowired
    private PresupuestoServices presupuestoServices;
    @Autowired
    private PresupuestoRepository presupuestoRepository;

    private String token;

    @Autowired
    private JwtUtils jwtUtils;

// para crear el presupuesto
    @PostMapping
    public ResponseEntity<?> createPresupuesto(@RequestBody PresupuestoDto presupuestoDto) {
        try {
            presupuestoServices.savePresupuesto(presupuestoDto);
            return ResponseEntity.ok("Presupuesto creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear el presupuesto: " + e.getMessage());
        }
      
    }

// para trear el presupuesto por id de la cuenta
    @GetMapping("/{idCuenta}")
    public ResponseEntity<?> getByPresupuesto(@PathVariable Long idCuenta) {
        try {
            return ResponseEntity.ok(presupuestoServices.getPresupuestos(idCuenta));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener el presupuesto: " + e.getMessage());
        }
    }

    @GetMapping("/getUser")
    public ResponseEntity<?> getPresupuestoUser(HttpServletRequest request) {
        token = jwtUtils.getJwtFromRequest(request);

    if (token != null && jwtUtils.validateJwtToken(token)) {
        Long userId = jwtUtils.getUserIdFromJwtToken(token);

        // Buscar al usuario en la base de datos
        List<Presupuesto> presupuestos = presupuestoRepository.findByUsuarioId(userId);
                
        return ResponseEntity.ok(presupuestos);
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }
    
// para acutualizar el presupuesto por id
    @PutMapping("{id}")
    public ResponseEntity<?> updatePresupuesto(@PathVariable Long id, @RequestBody PresupuestoDto presupuestoDto) {

        try {
            presupuestoServices.updatePresupuesto(id, presupuestoDto);
            return ResponseEntity.ok("Presupuesto actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar el presupuesto: " + e.getMessage());
        }
    }

// opara eliminar el presupuesto por id
    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePresupuesto(@PathVariable Long id) {
        try {
            presupuestoServices.deletePresupuesto(id);
            return ResponseEntity.ok("Presupuesto eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar el presupuesto: " + e.getMessage());
        }
    }
}