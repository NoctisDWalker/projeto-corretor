package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.controller.docs.ContasDoc;
import com.nerdev.auxcorretor.dto.conta.ContaCreateRequestDTO;
import com.nerdev.auxcorretor.dto.conta.ContaResponseDTO;
import com.nerdev.auxcorretor.dto.conta.ContaUpdateRequestDTO;
import com.nerdev.auxcorretor.model.enums.StatusContaEnum;
import com.nerdev.auxcorretor.security.annotations.permissions.contas.PodeAtualizarConta;
import com.nerdev.auxcorretor.security.annotations.permissions.contas.PodeCadastrarConta;
import com.nerdev.auxcorretor.security.annotations.permissions.contas.PodeCancelarConta;
import com.nerdev.auxcorretor.security.annotations.permissions.contas.PodeSuspenderConta;
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
public class ContaController implements ContasDoc {

    private final ContaService contaService;
    private final RestLocationBuilder locationBuilder;

    @Override
    @PostMapping
    @PodeCadastrarConta
    public ResponseEntity<ContaResponseDTO> salvarConta(@RequestBody @Valid ContaCreateRequestDTO dto){

        ContaResponseDTO salva = contaService.criarConta(dto);
        URI location = locationBuilder.build(salva.contaId());

        return ResponseEntity.created(location).body(salva);
    }

    @Override
    @PatchMapping("/{id}/atualizar")
    @PodeAtualizarConta
    public ResponseEntity<ContaResponseDTO> atualizarConta(@PathVariable UUID id,
                                                            @RequestBody @Valid ContaUpdateRequestDTO dto){
        ContaResponseDTO atualizado = contaService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @Override
    @PatchMapping("/{id}/cancelar")
    @PodeCancelarConta
    public ResponseEntity<Void> cancelarConta(@PathVariable UUID id){
        contaService.inativar(id, StatusContaEnum.CANCELADA);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/suspender")
    @PodeSuspenderConta
    public ResponseEntity<Void> suspenderConta(@PathVariable UUID id){
        contaService.inativar(id, StatusContaEnum.SUSPENSA);
        return ResponseEntity.noContent().build();
    }

}
