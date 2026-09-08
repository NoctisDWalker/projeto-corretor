package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.dto.atendimento.AtendimentoCreateRequestDTO;
import com.nerdev.auxcorretor.dto.atendimento.AtendimentoFindResponseDTO;
import com.nerdev.auxcorretor.dto.atendimento.AtendimentoResponseDTO;
import com.nerdev.auxcorretor.dto.atendimento.AtendimentoUpdateRequestDTO;
import com.nerdev.auxcorretor.model.enums.StatusAtendimentoEnum;
import com.nerdev.auxcorretor.service.AtendimentoService;
import com.nerdev.auxcorretor.web.util.RestLocationBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/atendimentos")
@RequiredArgsConstructor
public class AtendimentoController {

    private final AtendimentoService atendimentoService;
    private final RestLocationBuilder locationBuilder;

    @PostMapping
    public ResponseEntity<AtendimentoResponseDTO> salvar(@RequestBody @Valid AtendimentoCreateRequestDTO dto) {
        AtendimentoResponseDTO salvo = atendimentoService.salvar(dto);
        URI location = locationBuilder.build(salvo.id());
        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtendimentoResponseDTO> atualizar(@PathVariable UUID id, @RequestBody @Valid AtendimentoUpdateRequestDTO dto) {
        AtendimentoResponseDTO atualizado = atendimentoService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        atendimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtendimentoResponseDTO> buscarPorId(@PathVariable UUID id) {
        AtendimentoResponseDTO atendimentoResponseDTO = atendimentoService.buscarPorId(id);
        return ResponseEntity.ok(atendimentoResponseDTO);
    }

    @GetMapping("/pesquisa")
    public ResponseEntity<Page<AtendimentoFindResponseDTO>> pesquisaAtendimentos(
            @RequestParam(value = "id-cliente", required = false) UUID idCliente,
            @RequestParam(value = "id-corretor", required = false) UUID idCorretor,
            @RequestParam(value = "status-atendimento", required = false) StatusAtendimentoEnum statusAtendimento,
            @RequestParam(value = "menor-data-cadastro", required = false) LocalDate menorDataCadastro,
            @RequestParam(value = "maior-data-cadastro", required = false) LocalDate maiorDataCadastro,
            @RequestParam(value = "menor-data-fim", required = false) LocalDate menorDataFim,
            @RequestParam(value = "maior-data-fim", required = false) LocalDate maiorDataFim,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho
    ) {
        Page<AtendimentoFindResponseDTO> resultado = atendimentoService.pesquisaAtendimento(
                idCliente, idCorretor, statusAtendimento, menorDataCadastro, maiorDataCadastro,
                menorDataFim, maiorDataFim, pagina, tamanho
        );
        return  ResponseEntity.ok(resultado);
    }

}
