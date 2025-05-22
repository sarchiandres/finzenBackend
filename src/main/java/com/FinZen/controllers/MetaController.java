package com.FinZen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.models.DTOS.MetaDto;
import com.FinZen.services.MetaServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/finzen/meta")
public class MetaController {

    @Autowired
    private MetaServices metaServices;

    // metodo para crear la meta

    @PostMapping
    public ResponseEntity<?> createMeta(@RequestBody MetaDto metaDto) {
       try {
            metaServices.createMeta(metaDto);
            return ResponseEntity.ok("Meta creada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
// metod para actualizar la meta 
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeta(@PathVariable Long id, @RequestBody MetaDto metaDto) {
        try {
            metaServices.updateMeta(id, metaDto);
            return ResponseEntity.ok("Meta actualizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{idCuenta}")
    public ResponseEntity<?> getMetaByIdUsuario(@PathVariable Long idCuenta) {
        try {
            return ResponseEntity.ok(metaServices.getAllMetas(idCuenta));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeta(@PathVariable Long id) {
        try {
            metaServices.deleteMeta(id);
            return ResponseEntity.ok("Meta eliminada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
