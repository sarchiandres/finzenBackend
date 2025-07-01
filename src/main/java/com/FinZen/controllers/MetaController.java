package com.FinZen.controllers;

import com.FinZen.models.DTOS.MetaDto;
import com.FinZen.models.Entities.Meta;
import com.FinZen.services.MetaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/finzen/meta")
@CrossOrigin(origins = "*")
public class MetaController {

    @Autowired
    private MetaServices metaServices;

    @PostMapping
    public ResponseEntity<?> crearMeta(@Valid @RequestBody MetaDto metaDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                result.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );
                return ResponseEntity.badRequest().body(errors);
            }

            if (metaDto.getFechaLimite() != null && metaDto.getFechaInicio() != null &&
                    metaDto.getFechaLimite().isBefore(metaDto.getFechaInicio())) {
                return ResponseEntity.badRequest()
                        .body("La fecha límite no puede ser anterior a la fecha de inicio");
            }

            Meta metaCreada = metaServices.createMeta(metaDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(metaCreada);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + e.getMessage());
        }
    }

    @GetMapping("/{idCuenta}")
    public ResponseEntity<?> obtenerMetas(@PathVariable Long idCuenta) {
        try {
            if (idCuenta == null || idCuenta <= 0) {
                return ResponseEntity.badRequest().body("ID de cuenta inválido");
            }

            return ResponseEntity.ok(metaServices.getAllMetas(idCuenta));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener las metas: " + e.getMessage());
        }
    }

    @DeleteMapping("/{idMeta}")
    public ResponseEntity<?> eliminarMeta(@PathVariable Long idMeta) {
        try {
            if (idMeta == null || idMeta <= 0) {
                return ResponseEntity.badRequest().body("ID de meta inválido");
            }

            metaServices.deleteMeta(idMeta);
            return ResponseEntity.ok(Map.of(
                    "message", "Meta eliminada exitosamente",
                    "idMeta", idMeta
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la meta: " + e.getMessage());
        }
    }

    @PutMapping("/{idMeta}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long idMeta, @RequestBody Map<String, String> request) {
        try {
            if (idMeta == null || idMeta <= 0) {
                return ResponseEntity.badRequest().body("ID de meta inválido");
            }

            String estado = request.get("estado");
            if (estado == null || estado.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Estado es requerido");
            }

            if (!estado.matches("^(creado|iniciado|terminado)$")) {
                return ResponseEntity.badRequest()
                        .body("Estado inválido. Valores permitidos: creado, iniciado, terminado");
            }

            metaServices.updateEstado(idMeta, estado);
            return ResponseEntity.ok(Map.of(
                    "message", "Estado actualizado exitosamente",
                    "idMeta", idMeta,
                    "nuevoEstado", estado
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el estado: " + e.getMessage());
        }
    }

    @PutMapping("/{idMeta}/aporte")
    public ResponseEntity<?> agregarAporte(@PathVariable Long idMeta, @RequestBody Map<String, Object> request) {
        try {
            if (idMeta == null || idMeta <= 0) {
                return ResponseEntity.badRequest().body("ID de meta inválido");
            }

            Object montoObj = request.get("montoAhorrado");
            if (montoObj == null) {
                return ResponseEntity.badRequest().body("Monto del aporte es requerido");
            }

            BigDecimal monto;
            try {
                if (montoObj instanceof Number) {
                    monto = new BigDecimal(montoObj.toString());
                } else if (montoObj instanceof String) {
                    String montoStr = montoObj.toString().replaceAll("[,$]", "");
                    monto = new BigDecimal(montoStr);
                } else {
                    return ResponseEntity.badRequest().body("Formato de monto inválido");
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Monto inválido: " + montoObj);
            }

            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body("El monto debe ser mayor a 0");
            }

            MetaDto metaActualizada = metaServices.agregarAporte(idMeta, monto);

            return ResponseEntity.ok(Map.of(
                    "message", "Aporte realizado exitosamente",
                    "idMeta", idMeta,
                    "montoAportado", monto,
                    "nuevoMontoAhorrado", metaActualizada.getMontoAhorrado(),
                    "porcentajeCompletado", metaActualizada.calcularPorcentajeCompletado()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al realizar el aporte: " + e.getMessage());
        }
    }

    @GetMapping("/cuenta/{idCuenta}/proximas-vencer")
    public ResponseEntity<?> obtenerProximasMetas(@PathVariable Long idCuenta,
                                                  @RequestParam(defaultValue = "30") int dias) {
        try {
            if (idCuenta == null || idCuenta <= 0) {
                return ResponseEntity.badRequest().body("ID de cuenta inválido");
            }

            if (dias <= 0 || dias > 365) {
                return ResponseEntity.badRequest()
                        .body("Días debe estar entre 1 y 365");
            }

            return ResponseEntity.ok(metaServices.getProximasMetas(idCuenta, dias));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener las metas próximas: " + e.getMessage());
        }
    }

    @GetMapping("/cuenta/{idCuenta}/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas(@PathVariable Long idCuenta) {
        try {
            if (idCuenta == null || idCuenta <= 0) {
                return ResponseEntity.badRequest().body("ID de cuenta inválido");
            }

            return ResponseEntity.ok(metaServices.getEstadisticas(idCuenta));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener las estadísticas: " + e.getMessage());
        }
    }
}