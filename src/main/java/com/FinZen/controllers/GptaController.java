package com.FinZen.controllers;

import com.FinZen.models.DTOS.PrompResponseDto;
import com.FinZen.models.DTOS.PromptRequestDto;
import com.FinZen.services.GptServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/finzen/gpt")
public class GptaController {

    @Autowired
    private GptServices gptServices;

    // metodo par el chat de usuario registrado
    @PostMapping("/{id}")
    public ResponseEntity<PrompResponseDto> promptOpenAiConContexto(@RequestBody PromptRequestDto requestDto, @PathVariable Long id) {
        PrompResponseDto responseDto = gptServices.sendRequestToOpenAi(requestDto, id);
        return ResponseEntity.ok(responseDto);
    }

    // chat para usuarios no registrados 
    @PostMapping
    public ResponseEntity<PrompResponseDto> promptOpenAiSinContexto(@RequestBody PromptRequestDto requestDto) {
        PrompResponseDto responseDto = gptServices.sendRequestToOpenAi2(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
