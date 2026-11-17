package com.nerdev.auxcorretor.controller.docs;

import com.nerdev.auxcorretor.dto.atendimento.AtendimentoCreateRequestDTO;
import com.nerdev.auxcorretor.dto.atendimento.AtendimentoFindResponseDTO;
import com.nerdev.auxcorretor.dto.atendimento.AtendimentoResponseDTO;
import com.nerdev.auxcorretor.dto.atendimento.AtendimentoUpdateRequestDTO;
import com.nerdev.auxcorretor.model.enums.StatusAtendimentoEnum;
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
import java.util.UUID;

import static com.nerdev.auxcorretor.controller.docs.SwaggerConstants.*;

@Tag(name = "Atendimento", description = "Gerenciamento da carteira de atendimentos")
@SecurityRequirement(name = "bearerAuth")
public interface AtendimentosDoc {

    @Operation(summary = "Iniciar atendimento", description = "Cria um novo atendimento vinculado a um cliente do corretor autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Atendimento iniciado com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado ou não pertence ao corretor autenticado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada: campos obrigatórios ausentes, cliente inativo ou já existe um atendimento ativo para este cliente.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<AtendimentoResponseDTO> salvar(AtendimentoCreateRequestDTO dto);

    @Operation(summary = "Atualizar atendimento", description = "Atualiza os dados ou o status de um atendimento existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Atendimento atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Operação não permitida: atendimentos finalizados não podem ser alterados.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<AtendimentoResponseDTO> atualizar(
            @Parameter(description = "ID único do atendimento") UUID id,
            AtendimentoUpdateRequestDTO dto
    );

    @Operation(summary = "Excluir atendimento", description = "Exclui um atendimento caso não possua visitas vinculadas.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atendimento excluído com sucesso.", content = @Content),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Operação não permitida: o atendimento possui visitas vinculadas e não pode ser excluído.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<Void> deletar(@Parameter(description = "ID único do atendimento") UUID id);

    @Operation(summary = "Buscar atendimento por ID", description = "Retorna os detalhes de um atendimento específico pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalhes do atendimento retornados com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<AtendimentoResponseDTO> buscarPorId(@Parameter(description = "ID único do atendimento") UUID id);

    @Operation(summary = "Pesquisar atendimentos", description = "Pesquisa paginada de atendimentos com filtros opcionais.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca de atendimentos realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<Page<AtendimentoFindResponseDTO>> pesquisaAtendimentos(
            @Parameter(description = "Filtro de atendimento por cliente") UUID idCliente,
            @Parameter(description = "Filtro de atendimento por status") StatusAtendimentoEnum statusAtendimento,
            @Parameter(description = "Filtra atendimento por data de cadastro (mínimo desejado)") LocalDate menorDataCadastro,
            @Parameter(description = "Filtra atendimento por data de cadastro (máximo desejado)") LocalDate maiorDataCadastro,
            @Parameter(description = "Filtra atendimento por data final (mínimo desejado)") LocalDate menorDataFim,
            @Parameter(description = "Filtra atendimento por data final (máximo desejado)") LocalDate maiorDataFim,
            @Parameter(description = "Número da página (inicia em 0)") Integer pagina,
            @Parameter(description = "Quantidade de registros por página") Integer tamanho
    );

}
