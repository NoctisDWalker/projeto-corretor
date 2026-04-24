package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.dto.VisitaCreateRequestDTO;
import com.nerdev.auxcorretor.dto.VisitaResponseDTO;
import com.nerdev.auxcorretor.dto.VisitaUpdateRequestDTO;
import com.nerdev.auxcorretor.service.VisitaService;
import com.nerdev.auxcorretor.web.util.RestLocationBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/atendimentos/{idAtendimento}/visitas")
@RequiredArgsConstructor
public class VisitaController {

    private final VisitaService visitaService;
    private final RestLocationBuilder locationBuilder;

    @PostMapping
    public ResponseEntity<VisitaResponseDTO> novaVisita(
            @PathVariable("idAtendimento") UUID idAtendimento,
            @RequestBody @Valid VisitaCreateRequestDTO createDto) {

        VisitaResponseDTO salvo = visitaService.salvar(idAtendimento, createDto);
        URI location = locationBuilder.build(salvo.id());
        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{idVisita}")
    public ResponseEntity<VisitaResponseDTO> atualizaVisita(@PathVariable UUID idAtendimento,
                                                            @PathVariable UUID idVisita,
                                                            @RequestBody @Valid VisitaUpdateRequestDTO updateDto) {
        VisitaResponseDTO atualizado = visitaService.atualizar(idVisita, updateDto, idAtendimento);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{idVisita}")
    ResponseEntity<Void> removerVisita(@PathVariable UUID idVisita,
                                                    @PathVariable UUID idAtendimento) {
        visitaService.cancelar(idVisita, idAtendimento);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{idVisita}/reativar")
    public ResponseEntity<Void> reativarVisita(@PathVariable UUID idAtendimento, @PathVariable UUID idVisita) {
        visitaService.reativarVisita(idVisita, idAtendimento);
        return ResponseEntity.noContent().build();
    }

}
