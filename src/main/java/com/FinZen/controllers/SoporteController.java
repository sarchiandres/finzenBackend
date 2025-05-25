package com.FinZen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.FinZen.services.SoporteServices;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/finzen/soporte")
public class SoporteController {
    @Autowired
    private SoporteServices soporteServices;

    @GetMapping
    public ResponseEntity<?> getMethodName() {
        try {
            return ResponseEntity.ok(soporteServices.getSoportesByUsuario());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
    
}
