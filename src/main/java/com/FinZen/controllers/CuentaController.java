
package com.FinZen.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.FinZen.models.DTOS.CuentaDto;
import com.FinZen.models.Entities.Cuenta;
import com.FinZen.security.Jwt.JwtUtils;
import com.FinZen.services.CuentaServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/finzen/cuentas")
@CrossOrigin(origins = "*") // Permitir CORS si es necesario
public class CuentaController {

    @Autowired
    private CuentaServices cuentaServices;

    @Autowired
    private JwtUtils jwtUtils;

    // Método para traer las cuentas por id de usuario
    @GetMapping
    public ResponseEntity<?> getCuentas(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            List<Cuenta> cuentas = cuentaServices.getAccountByUserId(userId);
            return ResponseEntity.ok(cuentas);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }

    // Método para crear la cuenta
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody CuentaDto cuentaDto, HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);

            // Establecer el userId en el DTO
            cuentaDto.setIdUsuario(userId);

            // Crear la cuenta y devolver la cuenta creada (no solo un mensaje)
            Cuenta cuentaCreada = cuentaServices.createAccount(cuentaDto);
            return ResponseEntity.ok(cuentaCreada);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error al crear la cuenta: Token inválido");
    }

    // Método para actualizar la cuenta
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody CuentaDto cuentaDto, HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            try {
                Cuenta cuentaActualizada = cuentaServices.updCuenta(id, cuentaDto);
                return ResponseEntity.ok(cuentaActualizada);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al actualizar la cuenta: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }

    // Método para eliminar la cuenta
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id, HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            try {
                cuentaServices.deleteAccount(id);
                return ResponseEntity.ok("Cuenta eliminada exitosamente");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al eliminar la cuenta: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }
}