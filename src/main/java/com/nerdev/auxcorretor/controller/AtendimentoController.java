package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.dto.AtendimentoCreateRequestDTO;
import com.nerdev.auxcorretor.dto.AtendimentoResponseDTO;
import com.nerdev.auxcorretor.dto.AtendimentoUpdateRequestDTO;
import com.nerdev.auxcorretor.service.AtendimentoService;
import com.nerdev.auxcorretor.web.util.RestLocationBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/atendimentos")
@RequiredArgsConstructor
public class AtendimentoController {

    private final AtendimentoService atendimentoService;
    private final RestLocationBuilder locationBuilder;

    @PostMapping
    public ResponseEntity<AtendimentoResponseDTO> salvar (@RequestBody @Valid AtendimentoCreateRequestDTO dto){
        AtendimentoResponseDTO salvo = atendimentoService.salvar(dto);
        URI location = locationBuilder.build(salvo.id());
        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtendimentoResponseDTO> atualizar(@PathVariable UUID id, @RequestBody @Valid AtendimentoUpdateRequestDTO dto){
        AtendimentoResponseDTO atualizado = atendimentoService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id){
        atendimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtendimentoResponseDTO> buscarPorId(@PathVariable UUID id){
        AtendimentoResponseDTO atendimentoResponseDTO = atendimentoService.buscarPorId(id);
        return ResponseEntity.ok(atendimentoResponseDTO);
    }

}
