package com.FinZen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.models.DTOS.IngresosDto;
import com.FinZen.services.IngresoServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping("/finzen/ingreso")
public class ingresoController {

    @Autowired
    private IngresoServices ingresoServices;

    // para crear el ingreso
    @PostMapping
    public ResponseEntity<?> CreateIngreso(@RequestBody IngresosDto inigreso) {
       try {
            ingresoServices.createIngreso(inigreso);
            return ResponseEntity.ok("Ingreso creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear el ingreso: " + e.getMessage());
        }
    }

// se trae los ingresos por presupuesto 

    @GetMapping("/{idPresupuesto}")
    public ResponseEntity<?> getIngresosByPresupuestoId(@PathVariable Long idPresupuesto) {
        try {
            return ResponseEntity.ok(ingresoServices.getAllIngresos(idPresupuesto));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener los ingresos: " + e.getMessage());
        }
    }
    
    // para editar o actualizar el ingreso 


     @PutMapping("/{idIngreso}") // id de el ingreso a actualizar 
    public String putMethodName(@PathVariable Long idIngreso , @RequestBody IngresosDto ingreso) {
        try {
            ingresoServices.updateIngreso(idIngreso, ingreso);
            return "Ingreso actualizado exitosamente";
        } catch (Exception e) {
            return "Error al actualizar el ingreso: " + e.getMessage();
        }
    }

    // para eliminar el ingreso

    @DeleteMapping("/{idIngreso}")
    public String deleteIngreso(@PathVariable Long idIngreso) {
        try {
            return ingresoServices.deleteIngreso(idIngreso);
        } catch (Exception e) {
            return "Error al eliminar el ingreso: " + e.getMessage();
        }
    }

    
}
