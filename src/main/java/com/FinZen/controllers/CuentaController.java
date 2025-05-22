package com.FinZen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.models.DTOS.CuentaDto;
import com.FinZen.services.CuentaServices;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




 @RestController
 @RequestMapping("/finzen/cuentas")
public class CuentaController {
    @Autowired
    private CuentaServices cuentaServices;


    // metodo para traer la cuentas por id de e usuarioo 

    @GetMapping("{idUsuario}")
    public ResponseEntity<?> getCuentas(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(cuentaServices.getAccountByUserId(idUsuario));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener las cuentas: " + e.getMessage());
        }
    }

    // metodo para crear la cuenta 
    @PostMapping
    public ResponseEntity<?>  createAccount(@RequestBody CuentaDto cuentaDto) {
        try {
            cuentaServices.createAccount(cuentaDto);
            return ResponseEntity.ok("Cuenta creada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear la cuenta: " + e.getMessage());
        }
    }
    
    // metodo para actualizar la cuenta
    @PutMapping("{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody CuentaDto cuentaDto) {
        try {
            cuentaServices.updCuenta(id, cuentaDto);
            return ResponseEntity.ok("Cuenta actualizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar la cuenta: " + e.getMessage());
        }

    }
    // metodo para eliminar la cuenta
    @DeleteMapping("{idCuenta}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long idCuenta) {
        try {
            cuentaServices.deleteAccount(idCuenta);
            return ResponseEntity.ok("Cuenta eliminada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar la cuenta: " + e.getMessage());
        }
    }
    
    
}
