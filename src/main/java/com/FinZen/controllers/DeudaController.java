package com.FinZen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.models.DTOS.DeudaDto;
import com.FinZen.services.DeudaServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/finzen/deuda")
public class DeudaController {
    @Autowired
    private DeudaServices deudaServices;

    // metodo para crear una deuda

    @PostMapping
    public ResponseEntity<?> createDeuda(@RequestBody DeudaDto deudaDto) {
        try {
            deudaServices.createDeuda(deudaDto);
            return ResponseEntity.ok("Deuda creada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // metodo para actualizar la deuda

    @PutMapping("/{id}")
    public ResponseEntity<?> putMethodName(@PathVariable String id, @RequestBody DeudaDto deudaDto) {
        try {
            deudaServices.updateDeuda(Long.parseLong(id), deudaDto);
            return ResponseEntity.ok("Deuda actualizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{idCuenta}")
    public ResponseEntity<?> getMethodName(@PathVariable Long idCuenta) {
        try {
            return ResponseEntity.ok(deudaServices.getDeudasByCuenta(idCuenta));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDeuda(@PathVariable Long id) {
        try {
            deudaServices.deleteDeuda(id);
            return ResponseEntity.ok("Deuda eliminada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    

}
