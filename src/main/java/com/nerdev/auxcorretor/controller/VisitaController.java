package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.controller.docs.VisitasDoc;
import com.nerdev.auxcorretor.dto.visita.*;
import com.nerdev.auxcorretor.model.enums.InteresseClienteEnum;
import com.nerdev.auxcorretor.model.enums.StatusVisitaEnum;
import com.nerdev.auxcorretor.security.annotations.permissions.visitas.*;
import com.nerdev.auxcorretor.security.annotations.permissions.visitas.historico.PodeAdicionarHistorico;
import com.nerdev.auxcorretor.security.annotations.permissions.visitas.historico.PodeBuscarTimeLine;
import com.nerdev.auxcorretor.service.VisitaService;
import com.nerdev.auxcorretor.web.util.RestLocationBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/atendimentos/{idAtendimento}/visitas")
@RequiredArgsConstructor
public class VisitaController implements VisitasDoc {

    private final VisitaService visitaService;
    private final RestLocationBuilder locationBuilder;

    @Override
    @PostMapping
    @PodeCadastrarVisita
    public ResponseEntity<VisitaResponseDTO> novaVisita(
            @PathVariable("idAtendimento") UUID idAtendimento,
            @RequestBody @Valid VisitaCreateRequestDTO createDto) {

        VisitaResponseDTO salvo = visitaService.salvar(idAtendimento, createDto);
        URI location = locationBuilder.build(salvo.id());
        return ResponseEntity.created(location).body(salvo);
    }

    @Override
    @PutMapping("/{idVisita}")
    @PodeAtualizarVisita
    public ResponseEntity<VisitaResponseDTO> atualizaVisita(@PathVariable UUID idAtendimento,
                                                            @PathVariable UUID idVisita,
                                                            @RequestBody @Valid VisitaUpdateRequestDTO updateDto) {
        VisitaResponseDTO atualizado = visitaService.atualizar(idVisita, updateDto, idAtendimento);
        return ResponseEntity.ok(atualizado);
    }

    @Override
    @DeleteMapping("/{idVisita}")
    @PodeRemoverVisita
    public ResponseEntity<Void> removerVisita(@PathVariable UUID idVisita,
                                       @PathVariable UUID idAtendimento) {
        visitaService.cancelar(idVisita, idAtendimento);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{idVisita}/reativar")
    @PodeReativarVisita
    public ResponseEntity<Void> reativarVisita(@PathVariable UUID idAtendimento, @PathVariable UUID idVisita) {
        visitaService.reativarVisita(idVisita, idAtendimento);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/pesquisa")
    @PodePesquisarVisita
    public ResponseEntity<Page<VisitaFindResponseDTO>> pesquisaVisita(
            @RequestParam(value = "id-atendimento", required = false) UUID idAtendimento,
            @RequestParam(value = "id-imovel", required = false) UUID idImovel,
            @RequestParam(value = "menor-data-agendada", required = false) LocalDate menorDataAgendada,
            @RequestParam(value = "maior-data-agendada", required = false) LocalDate maiorDataAgendada,
            @RequestParam(value = "menor-data-realizada", required = false) LocalDate menorDataRealizada,
            @RequestParam(value = "maior-data-realiada", required = false) LocalDate maiorDataRealizada,
            @RequestParam(value = "status-visita", required = false) StatusVisitaEnum statusVisita,
            @RequestParam(value = "interesse-cliente", required = false) InteresseClienteEnum interesseCliente,
            @RequestParam(value = "menor-data-cadastro", required = false) LocalDate menorDataCadastro,
            @RequestParam(value = "maior-data-cadastro", required = false) LocalDate maiorDataCadastro,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho
    ) {

        Page<VisitaFindResponseDTO> resultado = visitaService.pesquisaVisita(
                idAtendimento, idImovel, menorDataAgendada, maiorDataAgendada, menorDataRealizada, maiorDataRealizada,
                statusVisita, interesseCliente, menorDataCadastro, maiorDataCadastro, pagina, tamanho
        );
        return ResponseEntity.ok(resultado);
    }

    @Override
    @PostMapping("/{idVisita}/historicos")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PodeAdicionarHistorico
    public void adicionarHistoricoManual(@PathVariable UUID idVisita,
                                         @PathVariable UUID idAtendimento,
                                         @RequestBody @Valid RegistrarHistoricoVisitaRequestDTO historicoDTO
    ) {

        visitaService.adicionarHistoricoManual(idVisita, idAtendimento, historicoDTO.descricao());

    }

    @Override
    @GetMapping("/{idVisita}/historicos")
    @PodeBuscarTimeLine
    public ResponseEntity<List<HistoricoVisitaResponseDTO>> buscarTimeLine(@PathVariable UUID idVisita,
                                                                           @PathVariable UUID idAtendimento
    ) {
        List<HistoricoVisitaResponseDTO> listaTimeLine = visitaService.buscarTimeLineVisita(idVisita, idAtendimento);

        return ResponseEntity.ok(listaTimeLine);
    }

}
