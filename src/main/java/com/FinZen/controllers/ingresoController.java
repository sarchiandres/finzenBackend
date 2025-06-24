package com.FinZen.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.models.DTOS.IngresosDto;
import com.FinZen.models.Entities.Ingresos;
import com.FinZen.models.Entities.Usuarios;
import com.FinZen.repository.IngresosRepository;
import com.FinZen.security.Jwt.JwtUtils;
import com.FinZen.services.IngresoServices;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/finzen/ingreso")
public class ingresoController {

    @Autowired
    private IngresoServices ingresoServices;
        @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private IngresosRepository ingresosRepository;

    // para crear el ingreso
    @PostMapping
    public ResponseEntity<?> CreateIngreso(@RequestBody IngresosDto inigreso) {
       try {
            ingresoServices.createIngreso(inigreso);
            return ResponseEntity.ok("Ingreso creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear el ingreso: " + e.getMessage());
        }
    }

// se trae los ingresos por presupuesto 

    @GetMapping("/{idPresupuesto}")
    public ResponseEntity<?> getIngresosUserId(HttpServletRequest request) {
      String token = jwtUtils.getJwtFromRequest(request);

    if (token != null && jwtUtils.validateJwtToken(token)) {
        Long userId = jwtUtils.getUserIdFromJwtToken(token);

        // Buscar al usuario en la base de datos
        List<Ingresos> ingresos = ingresosRepository.findByUsuarioId(userId);
                
        return ResponseEntity.ok(ingresos);
    }
    return ResponseEntity.status(401).body("Token inválido o no proporcionado.");
    }
    
    // para editar o actualizar el ingreso 


     @PutMapping("/{idIngreso}") // id de el ingreso a actualizar 
    public String putMethodName(@PathVariable Long idIngreso , @RequestBody IngresosDto ingreso) {
        try {
            ingresoServices.updateIngreso(idIngreso, ingreso);
            return "Ingreso actualizado exitosamente";
        } catch (Exception e) {
            return "Error al actualizar el ingreso: " + e.getMessage();
        }
    }

    // para eliminar el ingreso

    @DeleteMapping("/{idIngreso}")
    public String deleteIngreso(@PathVariable Long idIngreso) {
        try {
            return ingresoServices.deleteIngreso(idIngreso);
        } catch (Exception e) {
            return "Error al eliminar el ingreso: " + e.getMessage();
        }
    }
}
