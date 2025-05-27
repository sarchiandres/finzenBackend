package com.FinZen.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.models.Entities.Gastos;

import com.FinZen.security.Jwt.JwtUtils;
import com.FinZen.services.PeridoMensualServices;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/finzen/informe")
public class PeriodoMensualController {
    @Autowired
    private PeridoMensualServices periodoMensualServices;
    @Autowired
    private JwtUtils jwtUtils;

    public String token;

    @GetMapping("/gasto/{anio}")
    public ResponseEntity<?> getMethodName(HttpServletRequest request, @PathVariable int anio) {
       token = jwtUtils.getJwtFromRequest(request);

    if (token != null && jwtUtils.validateJwtToken(token)) {
        Long userId = jwtUtils.getUserIdFromJwtToken(token);

       List<Gastos> periodo= periodoMensualServices.obGastosPorMes(anio,userId);
       
                
        return ResponseEntity.ok(periodo);
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }

    @GetMapping("/ingreso/{idUsuario}/{anio}")
    public ResponseEntity<?> getMethodName2(@PathVariable Long idUsuario, @PathVariable int anio) {
        try {
            return ResponseEntity.ok(periodoMensualServices.obIngresosPorMes(anio, idUsuario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/periodo/{idUsuario}")
    public ResponseEntity<?> getMethodName3(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(periodoMensualServices.getPeriodoMensual(idUsuario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
