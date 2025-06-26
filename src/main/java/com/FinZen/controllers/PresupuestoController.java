package com.FinZen.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.models.DTOS.PresupuestoDto; // DTO para recibir datos al crear/actualizar
import com.FinZen.models.DTOS.PresupuestoResponseDto; // DTO para enviar datos al frontend (con montoGastado)
import com.FinZen.models.Entities.Presupuesto; // Entidad (usada por save/update)
import com.FinZen.repository.PresupuestoRepository; // Se mantiene si usas sus métodos directos de búsqueda (no los del servicio)
import com.FinZen.security.Jwt.CustomUserDetails;
import com.FinZen.services.PresupuestoServices;

import jakarta.validation.Valid; // Importar para validación si usas @Valid en el DTO

@RestController
@RequestMapping("/finzen/presupuesto")
public class PresupuestoController {

    @Autowired
    private PresupuestoServices presupuestoServices;
    @Autowired
    private PresupuestoRepository presupuestoRepository; // Se mantiene para métodos que devuelven Presupuesto directamente
    // @Autowired private JwtUtils jwtUtils; // No es necesario inyectar JwtUtils si usas SecurityContextHolder

    // Endpoint para crear un nuevo presupuesto
    @PostMapping
    public ResponseEntity<?> createPresupuesto(@Valid @RequestBody PresupuestoDto presupuestoDto) { // Agregado @Valid
        try {
            Presupuesto savedPresupuesto = presupuestoServices.savePresupuesto(presupuestoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPresupuesto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor al crear el presupuesto: " + e.getMessage()));
        }
    }

    // Endpoint para actualizar un presupuesto existente
    @PutMapping("{id}") // Corregido a @PathVariable para consistencia
    public ResponseEntity<?> updatePresupuesto(@PathVariable Long id, @Valid @RequestBody PresupuestoDto presupuestoDto) { // Agregado @Valid
        try {
            Presupuesto updatedPresupuesto = presupuestoServices.updatePresupuesto(id, presupuestoDto);
            return ResponseEntity.ok(updatedPresupuesto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor al actualizar el presupuesto: " + e.getMessage()));
        }
    }

    // --- ¡¡¡ENDPOINT PRINCIPAL ACTUALIZADO!!! ---
    // Este endpoint es el que tu frontend debería llamar para obtener los presupuestos
    // con el monto gastado calculado.
    @GetMapping("/user-budgets") // Ruta más descriptiva y común
    public ResponseEntity<?> getPresupuestosForAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verificación de autenticación y tipo de principal
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "error", "Usuario no autenticado o tipo de principal inválido."
            ));
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId(); // Obtiene el ID del usuario autenticado

        try {
            // LLAMADA CLAVE: Usa el servicio para obtener la lista de PresupuestoResponseDto
            List<PresupuestoResponseDto> presupuestos = presupuestoServices.getPresupuestosForUser(userId);

            if (presupuestos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of(
                    "message", "No se encontraron presupuestos para el usuario."
                )); // 204 No Content si no hay presupuestos
            }
            return ResponseEntity.ok(presupuestos); // Devuelve la lista de DTOs con montoGastado
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Error al obtener los presupuestos del usuario: " + e.getMessage()
            ));
        }
    }
    // --- FIN ENDPOINT PRINCIPAL ACTUALIZADO ---

    // Endpoint para obtener un solo presupuesto por ID con sus gastos calculados
    @GetMapping("/{id}")
    public ResponseEntity<?> getPresupuestoById(@PathVariable Long id) {
        try {
            PresupuestoResponseDto presupuesto = presupuestoServices.getPresupuestoByIdWithGastos(id); // Llama al método del servicio
            return ResponseEntity.ok(presupuesto); // Devuelve el DTO con montoGastado
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al obtener el presupuesto: " + e.getMessage()));
        }
    }

    // Endpoint para eliminar un presupuesto por ID
    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePresupuesto(@PathVariable Long id) {
        try {
            String result = presupuestoServices.deletePresupuesto(id);
            return ResponseEntity.ok(Map.of("message", result)); // Envuelve el mensaje en un Map para consistencia
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor al eliminar el presupuesto: " + e.getMessage()));
        }
    }

    // --- Métodos de búsqueda por ID de Cuenta/Tarjeta/Inversión (Devuelven la entidad Presupuesto) ---
    // Si estos endpoints también necesitaran el 'montoGastado', tendrías que crear métodos
    // en PresupuestoServices que devuelvan List<PresupuestoResponseDto> para ellos,
    // siguiendo la misma lógica que getPresupuestosForUser.

    @GetMapping("/getCuenta/{idCuenta}")
    public ResponseEntity<?> getByPresupuestoCuenta(@PathVariable Long idCuenta) {
        try {
            List<Presupuesto> presupuestos = presupuestoServices.getPresupuestosByCuenta(idCuenta);
            if (presupuestos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "No se encontraron presupuestos para la cuenta."));
            }
            return ResponseEntity.ok(presupuestos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al obtener los presupuestos por cuenta: " + e.getMessage()));
        }
    }

    @GetMapping("/getTarjeta/{idTarjeta}")
    public ResponseEntity<?> getByTarjeta(@PathVariable Long idTarjeta) {
        try {
            List<Presupuesto> presupuestos = presupuestoServices.getPresupuestosByTarjeta(idTarjeta);
            if (presupuestos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "No se encontraron presupuestos para la tarjeta."));
            }
            return ResponseEntity.ok(presupuestos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al obtener los presupuestos por tarjeta: " + e.getMessage()));
        }
    }

    @GetMapping("/getInversiones/{idInversion}")
    public ResponseEntity<?> getByInversiones(@PathVariable Long idInversion) {
        try {
            List<Presupuesto> presupuestos = presupuestoServices.getPresupuestosByInversion(idInversion);
            if (presupuestos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "No se encontraron presupuestos para la inversión."));
            }
            return ResponseEntity.ok(presupuestos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al obtener los presupuestos por inversión: " + e.getMessage()));
        }
    }
}