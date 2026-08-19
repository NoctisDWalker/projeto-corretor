package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.dto.conta.ContaCreateRequestDTO;
import com.nerdev.auxcorretor.dto.conta.ContaResponseDTO;
import com.nerdev.auxcorretor.dto.conta.ContaUpdateRequestDTO;
import com.nerdev.auxcorretor.model.enums.StatusContaEnum;
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

    @PatchMapping("/{id}/atualizar")
    public ResponseEntity<ContaResponseDTO> atualizarConta(@PathVariable UUID id,
                                                            @RequestBody @Valid ContaUpdateRequestDTO dto){
        ContaResponseDTO atualizado = contaService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarConta(@PathVariable UUID id){
        contaService.inativar(id, StatusContaEnum.CANCELADA);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspender")
    public ResponseEntity<Void> suspenderConta(@PathVariable UUID id){
        contaService.inativar(id, StatusContaEnum.SUSPENSA);
        return ResponseEntity.noContent().build();
    }

}
