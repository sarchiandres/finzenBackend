package com.FinZen.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.FinZen.models.DTOS.InversionDto;
import com.FinZen.models.Entities.Inversion;
import com.FinZen.security.Jwt.JwtUtils;
import com.FinZen.services.InversionServices;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/finzen/inversiones")
@CrossOrigin(origins = "*") // Permitir CORS si es necesario
public class InversionController {

    @Autowired
    private InversionServices inversionServices;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<?> getInversiones(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            List<Inversion> inversiones = inversionServices.getInversionesByUserId(userId);
            return ResponseEntity.ok(inversiones);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }

    @PostMapping
    public ResponseEntity<?> createInversion(@RequestBody InversionDto inversionDto, HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            inversionDto.setIdUsuario(userId);
            Inversion inversion = inversionServices.createInversion(inversionDto);
            return ResponseEntity.ok(inversion); // Devolver la inversión creada, no solo mensaje
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error al crear la inversión: Token inválido");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInversion(@PathVariable Long id, @RequestBody InversionDto inversionDto, HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            try {
                inversionServices.updateInversion(id, inversionDto);
                return ResponseEntity.ok("Inversion Actualizada"); // Devolver la inversión actualizada
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al actualizar la inversión: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInversion(@PathVariable Long id, HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            try {
                inversionServices.deleteInversion(id);
                return ResponseEntity.ok("Inversión eliminada exitosamente");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al eliminar la inversión: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }
}