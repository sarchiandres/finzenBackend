package com.FinZen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.services.PeridoMensualServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/finzen/informe")
public class PeriodoMensualController {
    @Autowired
    private PeridoMensualServices periodoMensualServices;

    @GetMapping("/gasto/{idUsuario}/{anio}")
    public ResponseEntity<?> getMethodName(@PathVariable Long idUsuario, @PathVariable int anio) {
        try {
            return ResponseEntity.ok(periodoMensualServices.obGastosPorMes(anio, idUsuario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
