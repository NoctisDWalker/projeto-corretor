package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.dto.conta.ContaCreateRequestDTO;
import com.nerdev.auxcorretor.dto.conta.ContaResponseDTO;
import com.nerdev.auxcorretor.service.ContaService;
import com.nerdev.auxcorretor.web.util.RestLocationBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contas")
public class ContaController {

    private final ContaService contaService;
    private final RestLocationBuilder locationBuilder;

    @PostMapping
    public ResponseEntity<ContaResponseDTO> salvarConta(@RequestBody @Valid ContaCreateRequestDTO dto){

        ContaResponseDTO salva = contaService.criarConta(dto);
        URI location = locationBuilder.build(salva.contaId());

        return ResponseEntity.created(location).body(salva);
    }

}
