package com.nerdev.auxcorretor.controller.docs;

import com.nerdev.auxcorretor.dto.corretor.CorretorFindResponseDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorResponseDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorUpdateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static com.nerdev.auxcorretor.controller.docs.SwaggerConstants.*;

@Tag(name = "Corretores", description = "Gestão de Corretores")
@SecurityRequirement(name = "bearerAuth")
public interface CorretoresDoc {

    @Operation(summary = "Atualizar corretor", description = "Atualiza os dados cadastrais de um corretor dentro do escopo da conta.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados do corretor atualizados com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Corretor não encontrado ou não pertence à conta do usuário autenticado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada: e-mail ou CPF já cadastrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<CorretorResponseDTO> atualizar(
            @Parameter(description = "ID único do corretor") UUID id,
            CorretorUpdateRequestDTO dto
    );

    @Operation(summary = "Inativar corretor", description = "Inativa logicamente um corretor pertencente à conta do usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Corretor inativado com sucesso.", content = @Content),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Corretor não encontrado ou não pertence à conta do usuário autenticado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Operação não permitida: o corretor já se encontra inativo.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<Void> deletar(@Parameter(description = "ID único do corretor") UUID id);

    @Operation(summary = "Reativar corretor", description = "Reativa um corretor inativo pertencente à conta do usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Corretor reativado com sucesso.", content = @Content),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Corretor não encontrado ou não pertence à conta do usuário autenticado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Operação não permitida: apenas corretores inativos podem ser reativados.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<Void> reativar(@Parameter(description = "ID único do corretor") UUID id);

    @Operation(summary = "Buscar corretor por ID", description = "Retorna os detalhes de um corretor específico dentro do escopo da conta.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalhes do corretor retornados com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Corretor não encontrado ou não pertence à conta do usuário autenticado.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<CorretorResponseDTO> buscarCorretorPorId(@Parameter(description = "ID único do corretor") UUID id);

    @Operation(summary = "Listar todos os corretores", description = "Retorna todos os corretores ativos vinculados à conta do usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listagem de corretores retornada com sucesso."),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<List<CorretorResponseDTO>> buscarTodos();

    @Operation(summary = "Pesquisar corretores", description = "Pesquisa paginada de corretores dentro do escopo da conta do usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca de corretores realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<Page<CorretorFindResponseDTO>> pesquisaCorretores(
            @Parameter(description = "Filtro por ID do corretor") UUID id,
            @Parameter(description = "Filtro por nome (parcial)") String nome,
            @Parameter(description = "Filtro por CPF") String cpf,
            @Parameter(description = "Filtro por CRECI") String creci,
            @Parameter(description = "Número da página (inicia em 0)") Integer pagina,
            @Parameter(description = "Quantidade de registros por página") Integer tamanho
    );

}
