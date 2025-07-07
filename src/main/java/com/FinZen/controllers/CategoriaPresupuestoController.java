package com.FinZen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.services.CategoriaPresupuestoServices;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/finzen/categoria-presupuesto")
public class CategoriaPresupuestoController {
    @Autowired
    private CategoriaPresupuestoServices categoriaPresupuestoServices;

    @GetMapping
    public ResponseEntity<?> getMethodName() {
        return ResponseEntity.ok(categoriaPresupuestoServices.getAllCategorias());
    }
    
}
