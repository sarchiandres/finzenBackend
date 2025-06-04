package com.FinZen.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.FinZen.models.DTOS.InformeDto;
import com.FinZen.security.Jwt.JwtUtils;
import com.FinZen.services.InformeService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/finzen/informes")
public class InformeController {

    @Autowired
    private InformeService informeService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/generar")
    public ResponseEntity<?> generarInforme(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);
        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            try {
                InformeDto informe = informeService.generarInformeCompleto(userId);
                return ResponseEntity.ok(informe);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al generar el informe: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token inválido o no proporcionado.");
    }

    @GetMapping
    public ResponseEntity<?> getInformes(HttpServletRequest request,
                                        @RequestParam(value = "usuarioId", required = false) Long usuarioId) {
        String token = jwtUtils.getJwtFromRequest(request);
        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long authUserId = jwtUtils.getUserIdFromJwtToken(token);
            Long targetUserId = (usuarioId != null) ? usuarioId : authUserId;
            try {
                List<InformeDto> informes = informeService.getInformesByUsuarioId(targetUserId);
                return ResponseEntity.ok(informes);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al obtener los informes: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token inválido o no proporcionado.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInformeById(HttpServletRequest request,
    @PathVariable("id") Long id,
        @RequestParam(value = "usuarioId", required = false) Long usuarioId) {
        String token = jwtUtils.getJwtFromRequest(request);
        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long authUserId = jwtUtils.getUserIdFromJwtToken(token);
            Long targetUserId = (usuarioId != null) ? usuarioId : authUserId;
            try {
                InformeDto informe = informeService.getInformeByIdAndUsuario(id, targetUserId);
                return ResponseEntity.ok(informe);
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al obtener el informe: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token inválido o no proporcionado.");
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> getInformeByIdForAdmin(@PathVariable("id") Long id) {
        try {
            InformeDto informe = informeService.getInformeByIdForAdmin(id);
            return ResponseEntity.ok(informe);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener el informe: " + e.getMessage());
        }
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> getAllInformesForAdmin() {
        try {
            List<InformeDto> informes = informeService.getAllInformesForAdmin();
            return ResponseEntity.ok(informes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los informes: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInforme(HttpServletRequest request, @PathVariable("id") Long id) {
        String token = jwtUtils.getJwtFromRequest(request);
        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            try {
                String mensaje = informeService.deleteInformeByIdAndUsuario(id, userId);
                return ResponseEntity.ok(mensaje);
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al eliminar el informe: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token inválido o no proporcionado.");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllInformes(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);
        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            try {
                String mensaje = informeService.deleteAllInformesByUsuario(userId);
                return ResponseEntity.ok(mensaje);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al eliminar los informes: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token inválido o no proporcionado.");
    }
}