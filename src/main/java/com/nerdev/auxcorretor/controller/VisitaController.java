package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.dto.*;
import com.nerdev.auxcorretor.model.historicos.HistoricoVisita;
import com.nerdev.auxcorretor.service.VisitaService;
import com.nerdev.auxcorretor.web.util.RestLocationBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
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

    @PostMapping("/{idVisita}/historicos")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adicionarHistoricoManual(@PathVariable UUID idVisita,
                                         @PathVariable UUID idAtendimento,
                                         @RequestBody @Valid RegistrarHistoricoVisitaRequestDTO historicoDTO
    ) {

        visitaService.adicionarHistoricoManual(idVisita, idAtendimento, historicoDTO.descricao());

    }

    @GetMapping("/{idVisita}/historicos")
    public ResponseEntity<List<HistoricoVisitaResponseDTO>> buscarTimeLine(@PathVariable UUID idVisita,
                                                                           @PathVariable UUID idAtendimento
    ) {
        List<HistoricoVisitaResponseDTO> listaTimeLine = visitaService.buscarTimeLineVisita(idVisita, idAtendimento);

        return ResponseEntity.ok(listaTimeLine);
    }

}
