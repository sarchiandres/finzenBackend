package com.FinZen.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.FinZen.models.DTOS.TarjetaDto;
import com.FinZen.models.Entities.Tarjeta;
import com.FinZen.security.Jwt.JwtUtils;
import com.FinZen.services.TarjetaServices;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/finzen/tarjetas")
@CrossOrigin(origins = "*") // Permitir CORS si es necesario
public class TarjetaController {

    @Autowired
    private TarjetaServices tarjetaServices;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<?> getTarjetas(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            List<Tarjeta> tarjetas = tarjetaServices.getTarjetasByUserId(userId);
            return ResponseEntity.ok(tarjetas);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }

    @PostMapping
    public ResponseEntity<?> createTarjeta(@RequestBody TarjetaDto tarjetaDto, HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            tarjetaDto.setIdUsuario(userId);
            Tarjeta tarjeta = tarjetaServices.createTarjeta(tarjetaDto);
            return ResponseEntity.ok(tarjeta);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error al crear la tarjeta: Token inválido");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTarjeta(@PathVariable Long id, @RequestBody TarjetaDto tarjetaDto, HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            try {
                tarjetaServices.updateTarjeta(id, tarjetaDto);
                return ResponseEntity.ok("Tarjeta Actualizada");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al actualizar la tarjeta: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTarjeta(@PathVariable Long id, HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            try {
                tarjetaServices.deleteTarjeta(id);
                return ResponseEntity.ok("Tarjeta eliminada exitosamente");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al eliminar la tarjeta: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }
}