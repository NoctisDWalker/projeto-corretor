package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.dto.auth.AtualizaSenhaDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorCreateRequestDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorResponseDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorUpdateRequestDTO;
import com.nerdev.auxcorretor.service.CorretorService;
import com.nerdev.auxcorretor.web.util.RestLocationBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/corretores")
@RequiredArgsConstructor
public class CorretorController {

    private final CorretorService corretorService;
    private final RestLocationBuilder locationBuilder;

    @PostMapping
    public ResponseEntity<CorretorResponseDTO> salvar(@RequestBody @Valid CorretorCreateRequestDTO dto) {
        CorretorResponseDTO corretorSalvo = corretorService.salvar(dto);
        URI location = locationBuilder.build(corretorSalvo.id());
        return ResponseEntity.created(location).body(corretorSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CorretorResponseDTO> atualizar(
            @PathVariable UUID id, @RequestBody @Valid CorretorUpdateRequestDTO dto) {

        CorretorResponseDTO corretorAtualizado = corretorService.atualizar(id, dto);
        return ResponseEntity.ok(corretorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {

        corretorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable UUID id) {
        corretorService.reativarCorretor(id);
        return ResponseEntity.noContent().build();
    }

}
