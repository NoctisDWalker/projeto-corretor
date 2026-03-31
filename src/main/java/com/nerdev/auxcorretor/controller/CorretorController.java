package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.dto.CorretorCreateRequestDTO;
import com.nerdev.auxcorretor.dto.CorretorResponseDTO;
import com.nerdev.auxcorretor.dto.CorretorUpdateRequestDTO;
import com.nerdev.auxcorretor.service.CorretorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/corretores")
@AllArgsConstructor
public class CorretorController {

    private CorretorService corretorService;

    @PostMapping
    public ResponseEntity<CorretorResponseDTO> salvar(@RequestBody @Valid CorretorCreateRequestDTO dto) {
        CorretorResponseDTO corretorSalvo = corretorService.salvar(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(corretorSalvo.uuid())
                .toUri();

        return ResponseEntity.created(location).body(corretorSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CorretorResponseDTO> atualizar(
            @PathVariable UUID id, @RequestBody @Valid CorretorUpdateRequestDTO dto) {

        corretorService.atualizar(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id){

        corretorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
