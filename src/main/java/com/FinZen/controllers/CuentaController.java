package com.FinZen.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.models.DTOS.CuentaDto;
import com.FinZen.models.Entities.Cuenta;
import com.FinZen.security.Jwt.JwtUtils;
import com.FinZen.services.CuentaServices;
import jakarta.servlet.http.HttpServletRequest;
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
    @Autowired
    private JwtUtils jwtUtils;
    public String token;


    // metodo para traer la cuentas por id de e usuarioo 

    @GetMapping
    public ResponseEntity<?> getCuentas(HttpServletRequest request) {
        token = jwtUtils.getJwtFromRequest(request);

    if (token != null && jwtUtils.validateJwtToken(token)) {
        Long userId = jwtUtils.getUserIdFromJwtToken(token);

        // Buscar al usuario en la base de datos
        List<Cuenta> cuentas = cuentaServices.getAccountByUserId(userId);
                
        return ResponseEntity.ok(cuentas);
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }

    // metodo para crear la cuenta 
    @PostMapping
    public ResponseEntity<?>  createAccount(@RequestBody CuentaDto cuentaDto, HttpServletRequest request) {

            token = jwtUtils.getJwtFromRequest(request);

            if (token != null && jwtUtils.validateJwtToken(token)) {
                Long userId = jwtUtils.getUserIdFromJwtToken(token);
        
                CuentaDto cuentas = new CuentaDto();
                cuentas.setIdUsuario(userId);
                cuentas.setMonto(cuentaDto.getMonto());
                cuentas.setNombre(cuentaDto.getNombre());
                
                cuentaServices.createAccount(cuentas);
            return ResponseEntity.ok("Cuenta creada exitosamente");
                
            }
            

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error al crear la cuenta: " );
        
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
