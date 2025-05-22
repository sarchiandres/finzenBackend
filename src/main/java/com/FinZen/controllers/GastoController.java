package com.FinZen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.models.DTOS.GastosDto;
import com.FinZen.services.GastosServices;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/finzen/gasto")
public class GastoController {
    
    @Autowired
    private GastosServices gastoServices;

    // metodo para crear el gasto
    @PostMapping
    public ResponseEntity<?> createGasto(@Valid  @RequestBody GastosDto gastosDto) {
        try {
            gastoServices.saveGasto(gastosDto);
            return ResponseEntity.ok("Gasto creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear el gasto: " + e.getMessage());
        }
    }
// metodo para actiualizar los gastos por id 
    @PutMapping("{id}")
    public ResponseEntity<?> putMethodName(@PathVariable Long id, @RequestBody GastosDto gastosDto) {
        try {
            gastoServices.updateGasto(id, gastosDto);
            return ResponseEntity.ok("Gasto actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar el gasto: " + e.getMessage());
        }
        
    }

// se trae los gastos por presupuesto
    @GetMapping("{id}")
    public ResponseEntity<?> getGastoByIdPresupuesto(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(gastoServices.getGastosByPresupuestoId(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener el gasto: " + e.getMessage());
        }
    }

    // metodo para eliminar el gasto
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteGasto(@PathVariable Long id) {
        try {
            gastoServices.deleteGasto(id);
            return ResponseEntity.ok("Gasto eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar el gasto: " + e.getMessage());
        }
    }
    

}
