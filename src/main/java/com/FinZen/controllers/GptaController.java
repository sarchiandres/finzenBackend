package com.FinZen.controllers;

import com.FinZen.models.DTOS.PrompResponseDto;
import com.FinZen.models.DTOS.PromptRequestDto;
import com.FinZen.models.Entities.Usuarios;
import com.FinZen.security.Jwt.JwtUtils;
import com.FinZen.services.GptServices;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/finzen/gpt")
public class GptaController {

    @Autowired
    private GptServices gptServices;
    @Autowired
    private JwtUtils jwtUtils;
    public String token;

    // metodo par el chat de usuario registrado
    @PostMapping("/user")
    public ResponseEntity<?> promptOpenAiConContexto(@RequestBody PromptRequestDto requestDto, HttpServletRequest request) {
      
        token = jwtUtils.getJwtFromRequest(request);

    if (token != null && jwtUtils.validateJwtToken(token)) {
        Long userId = jwtUtils.getUserIdFromJwtToken(token);

        PrompResponseDto responseDto = gptServices.sendRequestToOpenAi(requestDto, userId);
                
        return ResponseEntity.ok(responseDto);
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado.");
    }

    // chat para usuarios no registrados 
    @PostMapping("/noUser")
    public ResponseEntity<PrompResponseDto> promptOpenAiSinContexto(@RequestBody PromptRequestDto requestDto) {
        PrompResponseDto responseDto = gptServices.sendRequestToOpenAi2(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
