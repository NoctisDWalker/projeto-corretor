package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.dto.corretor.CorretorResponseDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorUpdateRequestDTO;
import com.nerdev.auxcorretor.service.CorretorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/corretores")
@RequiredArgsConstructor
public class CorretorController {

    private final CorretorService corretorService;


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
