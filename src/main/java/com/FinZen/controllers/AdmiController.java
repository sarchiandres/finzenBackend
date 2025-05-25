package com.FinZen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.models.DTOS.SoporteDto;
import com.FinZen.services.SoporteServices;

@RestController
@RequestMapping("/finzen/admi")
public class AdmiController {
    @Autowired
    private SoporteServices soporteServices;

    @PostMapping
    public ResponseEntity<?> postMethodName(@RequestBody SoporteDto soporteDto) {
       try {
            soporteServices.createSoporte(soporteDto);
            return ResponseEntity.ok("Soporte creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putMethodName(@PathVariable Long id, @RequestBody SoporteDto soporteDto) {
        try {
            soporteServices.updateSoporte(id, soporteDto);
            return ResponseEntity.ok("Soporte actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getMethodName() {
        try {
            return ResponseEntity.ok(soporteServices.getSoportesByUsuario());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
