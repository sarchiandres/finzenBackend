package com.FinZen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.FinZen.models.DTOS.InformeDto;
import com.FinZen.security.Jwt.JwtUtils;
import com.FinZen.services.InformeService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public ResponseEntity<?> getInformes(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            try {
                List<InformeDto> informes = informeService.getInformesByUsuarioId(userId);
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
    public ResponseEntity<?> getInformeById(HttpServletRequest request, @PathVariable Long id) {
        String token = jwtUtils.getJwtFromRequest(request);

        if (token != null && jwtUtils.validateJwtToken(token)) {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            try {
                InformeDto informe = informeService.getInformeByIdAndUsuario(id, userId);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInforme(HttpServletRequest request, @PathVariable Long id) {
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