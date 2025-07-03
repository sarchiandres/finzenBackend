package com.FinZen.controllers;

import com.FinZen.models.DTOS.MetaDto;
import com.FinZen.models.Entities.Meta;
import com.FinZen.security.Jwt.JwtUtils;
import com.FinZen.services.MetaServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/finzen/metas")
@CrossOrigin(origins = "*")
public class MetaController {

    @Autowired
    private MetaServices metaServices;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<?> getMetasByUser(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);
        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            List<Meta> metas = metaServices.getAllMetas(userId);
            return ResponseEntity.ok(metas);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }

    @PostMapping
    public ResponseEntity<?> createMeta(@RequestBody MetaDto metaDto, HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);
        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            if (!userId.equals(metaDto.getIdUsuario())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para crear metas para otro usuario.");
            }
            try {
                Meta metaCreada = metaServices.createMeta(metaDto);
                return ResponseEntity.ok(metaCreada);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> updateEstado(@PathVariable Long id, @RequestBody String nuevoEstado, HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);
        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            try {
                metaServices.updateEstado(id, nuevoEstado, userId);
                return ResponseEntity.ok("Estado actualizado exitosamente");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }

    @PutMapping("/{id}/aporte")
    public ResponseEntity<?> agregarAporte(@PathVariable Long id, @RequestBody BigDecimal montoAporte, HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);
        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            try {
                MetaDto metaActualizada = metaServices.agregarAporte(id, montoAporte, userId);
                return ResponseEntity.ok(metaActualizada);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeta(@PathVariable Long id, HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);
        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            try {
                metaServices.deleteMeta(id, userId);
                return ResponseEntity.ok("Meta eliminada exitosamente");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }

    @GetMapping("/proximas-vencer")
    public ResponseEntity<?> getProximasMetas(HttpServletRequest request, @RequestParam int dias) {
        String token = jwtUtils.getJwtFromRequest(request);
        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            List<Meta> metas = metaServices.getProximasMetas(userId, dias);
            return ResponseEntity.ok(metas);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<?> getEstadisticas(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);
        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            return ResponseEntity.ok(metaServices.getEstadisticas(userId));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }
}