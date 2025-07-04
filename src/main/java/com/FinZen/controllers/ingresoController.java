package com.FinZen.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.models.DTOS.IngresosDto;
import com.FinZen.models.Entities.Ingresos;
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

    @PostMapping
    public ResponseEntity<?> CreateIngreso(@RequestBody IngresosDto inigreso) {
       try {
            ingresoServices.createIngreso(inigreso);
            return ResponseEntity.ok("Ingreso creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear el ingreso: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getIngresosUserId(HttpServletRequest request) {
      String token = jwtUtils.getJwtFromRequest(request);

    if (token != null && jwtUtils.validateJwtToken(token)) {
        Long userId = jwtUtils.getUserIdFromJwtToken(token);

        List<Ingresos> ingresos = ingresosRepository.findByUsuarioId(userId);
                
        return ResponseEntity.ok(ingresos);
    }
    return ResponseEntity.status(401).body("Token inválido o no proporcionado.");
    }
    
    @PutMapping("/{idIngreso}")
    public String putMethodName(@PathVariable Long idIngreso , @RequestBody IngresosDto ingreso) {
        try {
            ingresoServices.updateIngreso(idIngreso, ingreso);
            return "Ingreso actualizado exitosamente";
        } catch (Exception e) {
            return "Error al actualizar el ingreso: " + e.getMessage();
        }
    }


      @GetMapping("/total")
    public ResponseEntity<?> getTotalIngresos(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No se pudo obtener el ID de usuario del token.");
            }

            Double totalIngresos = ingresosRepository.sumIngresosByUserId(userId);

            // Manejar el caso en que no haya ingresos (sumIngresosByUserId devolvería null)
            if (totalIngresos == null) {
                totalIngresos = 0.0; // Si no hay ingresos, la suma es 0
            }
            
            return ResponseEntity.ok(totalIngresos); // Devuelve solo el valor Double
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }

    @DeleteMapping("/{idIngreso}")
    public String deleteIngreso(@PathVariable Long idIngreso) {
        try {
            return ingresoServices.deleteIngreso(idIngreso);
        } catch (Exception e) {
            return "Error al eliminar el ingreso: " + e.getMessage();
        }
    }
}