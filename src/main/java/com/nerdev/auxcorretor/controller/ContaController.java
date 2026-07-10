package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.dto.conta.ContaCreateRequestDTO;
import com.nerdev.auxcorretor.dto.conta.ContaResponseDTO;
import com.nerdev.auxcorretor.dto.conta.ContaUpdateRequestDTO;
import com.nerdev.auxcorretor.service.ContaService;
import com.nerdev.auxcorretor.web.util.RestLocationBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

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

    @PatchMapping
    public  ResponseEntity<ContaResponseDTO> atualizarConta(@PathVariable UUID id,
                                                            @RequestBody @Valid ContaUpdateRequestDTO dto){
        ContaResponseDTO atualizado = contaService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

}
