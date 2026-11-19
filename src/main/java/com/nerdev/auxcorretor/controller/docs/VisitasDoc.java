package com.nerdev.auxcorretor.controller.docs;

import com.nerdev.auxcorretor.dto.visita.*;
import com.nerdev.auxcorretor.model.enums.InteresseClienteEnum;
import com.nerdev.auxcorretor.model.enums.StatusVisitaEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.nerdev.auxcorretor.controller.docs.SwaggerConstants.*;

@Tag(name = "Visitas", description = "Gestão de Visitas e Histórico de Visitas")
@SecurityRequirement(name = "bearerAuth")
public interface VisitasDoc {

    @Operation(summary = "Agendar visita", description = "Agenda uma nova visita associada a um atendimento ativo do corretor.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Visita agendada com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Atendimento ou imóvel não encontrado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada: atendimento já finalizado, imóvel privado ou finalizado por outro corretor, data obrigatória ou não futura, ou conflito/duplicidade de horário.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<VisitaResponseDTO> novaVisita(
            @Parameter(description = "Id único do atendimento.") UUID idAtendimento,
            VisitaCreateRequestDTO createDto
    );

    @Operation(summary = "Atualizar visita", description = "Atualiza os dados de agendamento, status ou resultado de uma visita.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visita atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado, visita não encontrada ou a visita não pertence ao atendimento informado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada: visita já finalizada, transição de status inválida, data realizada ausente/futura/anterior à agendada, data não futura ou horário duplicado.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<VisitaResponseDTO> atualizaVisita(
            @Parameter(description = "Id único do atendimento.") UUID idAtendimento,
            @Parameter(description = "Id único da visita.") UUID idVisita,
            VisitaUpdateRequestDTO updateDto
    );

    @Operation(summary = "Cancelar visita", description = "Cancela logicamente uma visita agendada.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Visita cancelada com sucesso.", content = @Content),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado, visita não encontrada ou a visita não pertence ao atendimento informado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Operação não permitida: a visita não pode ser cancelada no status atual ou o atendimento correspondente já foi finalizado.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<Void> removerVisita(
            @Parameter(description = "Id único da visita.") UUID idVisita,
            @Parameter(description = "Id único do atendimento.") UUID idAtendimento
    );
    @Operation(summary = "Reativar visita", description = "Reativa uma visita que foi cancelada anteriormente.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Visita reativada com sucesso.", content = @Content),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado, visita não encontrada ou a visita não pertence ao atendimento informado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Operação não permitida: a visita não pode ser reativada no status atual ou o atendimento correspondente já foi finalizado.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<Void> reativarVisita(
            @Parameter(description = "Id único do atendimento.") UUID idAtendimento,
            @Parameter(description = "Id único da visita.") UUID idVisita
    );

    @Operation(summary = "Pesquisar visitas", description = "Pesquisa paginada de visitas cruzando diversos filtros opcionais.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca de visitas realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<Page<VisitaFindResponseDTO>> pesquisaVisita(
            @Parameter(description = "Filtrar por ID do atendimento") UUID idAtendimento,
            @Parameter(description = "Filtrar por ID do imóvel") UUID idImovel,
            @Parameter(description = "Limite inferior da data agendada") LocalDate menorDataAgendada,
            @Parameter(description = "Limite superior da data agendada") LocalDate maiorDataAgendada,
            @Parameter(description = "Limite inferior da data realizada") LocalDate menorDataRealizada,
            @Parameter(description = "Limite superior da data realizada") LocalDate maiorDataRealizada,
            @Parameter(description = "Filtrar por status da visita") StatusVisitaEnum statusVisita,
            @Parameter(description = "Filtrar por nível de interesse do cliente") InteresseClienteEnum interesseCliente,
            @Parameter(description = "Limite inferior da data de cadastro") LocalDate menorDataCadastro,
            @Parameter(description = "Limite superior da data de cadastro") LocalDate maiorDataCadastro,
            @Parameter(description = "Número da página (inicia em 0)") Integer pagina,
            @Parameter(description = "Quantidade de registros por página") Integer tamanho
    );

    @Operation(summary = "Adicionar histórico manual", description = "Registra uma nova interação ou ocorrência no histórico de uma visita específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Histórico registrado na visita com sucesso.", content = @Content),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado, visita não encontrada ou a visita não pertence ao atendimento informado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada: a descrição do histórico está vazia ou fora do intervalo de 3 a 1000 caracteres.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    void adicionarHistoricoManual(
            @Parameter(description = "Id único da visita.") UUID idVisita,
            @Parameter(description = "Id único do atendimento.") UUID idAtendimento,
            RegistrarHistoricoVisitaRequestDTO historicoDTO
    );

    @Operation(summary = "Buscar linha do tempo da visita", description = "Retorna de forma cronológica todo o histórico e interações de uma visita específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Linha do tempo da visita retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado, visita não encontrada ou a visita não pertence ao atendimento informado.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<List<HistoricoVisitaResponseDTO>> buscarTimeLine(
            @Parameter(description = "Id único da visita.") UUID idVisita,
            @Parameter(description = "Id único do atendimento.") UUID idAtendimento
    );

}
