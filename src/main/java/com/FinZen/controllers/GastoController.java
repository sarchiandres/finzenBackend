package com.FinZen.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.models.DTOS.CategoriaGastoConConteoDTO;
import com.FinZen.models.DTOS.GastosDto;
import com.FinZen.models.DTOS.GastosResponseDto;
import com.FinZen.models.Entities.Gastos;
import com.FinZen.models.Entities.Ingresos;
import com.FinZen.repository.GastosRepository;
import com.FinZen.repository.IngresosRepository;
import com.FinZen.security.Jwt.JwtUtils;
import com.FinZen.services.GastosServices;
import com.FinZen.services.UsuariosServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/finzen/gasto")
public class GastoController {

    @Autowired
    private GastosServices gastoServices;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private IngresosRepository ingresosRepository;

    @Autowired
    private GastosRepository gastosRepository;

    @Autowired
    private UsuariosServices usuariosServices;

    @PostMapping
    public ResponseEntity<?> createGasto(@Valid  @RequestBody GastosDto gastosDto) {
        try {
            gastoServices.saveGasto(gastosDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Gasto creado exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor al crear el gasto: " + e.getMessage()));
        }
    }

    @GetMapping("/user/finances")
    public ResponseEntity<?> getUserFinances(HttpServletRequest request) {
        try {
            String token = jwtUtils.getJwtFromRequest(request);

            if (token != null && jwtUtils.validateJwtToken(token)) {
                Long userId = jwtUtils.getUserIdFromJwtToken(token);
                
                List<Ingresos> ingresos = ingresosRepository.findByUsuarioId(userId);
                List<Gastos> gastos = gastosRepository.getGastosByUsuarioId(userId);

                Map<String, Object> response = new HashMap<>();
                response.put("ingresos", ingresos);
                response.put("gastos", gastos);
                response.put("userId", userId);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "Token inválido o no proporcionado"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Error interno del servidor al obtener finanzas",
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/my-expenses")
    public ResponseEntity<?> getMyExpenses(HttpServletRequest request) {
        try {
            String token = jwtUtils.getJwtFromRequest(request);

            if (token != null && jwtUtils.validateJwtToken(token)) {
                Long userId = jwtUtils.getUserIdFromJwtToken(token);
                
                List<GastosResponseDto> gastos = gastoServices.getGastosForUser(userId);

                if (gastos.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of(
                        "message", "No se encontraron gastos para el usuario."
                    ));
                }
                return ResponseEntity.ok(gastos);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "Token inválido o no proporcionado"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Error al obtener los gastos del usuario: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/getCategoriasGasto")
    public ResponseEntity<?> getCategoriasGasto(HttpServletRequest request) {
        try {
            String token = jwtUtils.getJwtFromRequest(request);

            if (token != null && jwtUtils.validateJwtToken(token)) {
                Long userId = jwtUtils.getUserIdFromJwtToken(token);
                
                List<CategoriaGastoConConteoDTO> categoriasGasto = gastosRepository.findCategoriasWithExpenseCountsByUsuarioId(userId);
                
                if (categoriasGasto.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "No se encontraron categorías de gasto para el usuario."));
                }
                return ResponseEntity.ok(categoriasGasto);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token inválido o no proporcionado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al obtener las categorías de gasto: " + e.getMessage()));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateGasto(@PathVariable Long id, @Valid @RequestBody GastosDto gastosDto) {
        try {
            gastoServices.updateGasto(id, gastosDto);
            return ResponseEntity.ok(Map.of("message", "Gasto actualizado exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor al actualizar el gasto: " + e.getMessage()));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getGastoByIdPresupuesto(@PathVariable Long id) {
        try {
            List<Gastos> gastos = gastoServices.getGastosByPresupuestoId(id);
            if (gastos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "No se encontraron gastos para el presupuesto con ID: " + id));
            }
            return ResponseEntity.ok(gastos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al obtener los gastos por presupuesto: " + e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteGasto(@PathVariable Long id) {
        try {
            String result = gastoServices.deleteGasto(id);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor al eliminar el gasto: " + e.getMessage()));
        }
    }
}