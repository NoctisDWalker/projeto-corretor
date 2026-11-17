package com.nerdev.auxcorretor.controller.docs;

import com.nerdev.auxcorretor.dto.cliente.ClienteCreateRequestDTO;
import com.nerdev.auxcorretor.dto.cliente.ClienteFindResponseDto;
import com.nerdev.auxcorretor.dto.cliente.ClienteResponseDTO;
import com.nerdev.auxcorretor.dto.cliente.ClienteUpdateRequestDTO;
import com.nerdev.auxcorretor.model.enums.StatusClienteEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static com.nerdev.auxcorretor.controller.docs.SwaggerConstants.*;


@Tag(name = "Cliente", description = "Gerenciamento da carteira de clientes.")
@SecurityRequirement(name = "bearerAuth")
public interface ClientesDoc {

    @Operation(summary = "Cadastrar cliente", description = "Cadastra um novo cliente vinculado ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada: campos obrigatórios ausentes ou e-mail/CPF já cadastrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<ClienteResponseDTO> salvar(ClienteCreateRequestDTO dto);

    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente vinculado ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados do cliente atualizados com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado para o ID informado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada: e-mail ou CPF já pertence a outro cliente.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<ClienteResponseDTO> atualizar(
            @Parameter(description = "ID único do cliente") UUID id,
            ClienteUpdateRequestDTO dto
    );

    @Operation(summary = "Inativar cliente", description = "Inativa logicamente um cliente vinculado ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente inativado com sucesso.", content = @Content),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado para o ID informado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Operação não permitida: o cliente já se encontra inativo.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<Void> deletar(@Parameter(description = "ID único do cliente") UUID id);

    @Operation(summary = "Reativar cliente", description = "Reativa um cliente com status INATIVO vinculado ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente reativado com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado para o ID informado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Operação não permitida: apenas clientes com status INATIVO podem ser reativados.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<ClienteResponseDTO> reativar(@Parameter(description = "ID único do cliente") UUID id);

    @Operation(summary = "Pesquisar clientes", description = "Pesquisa paginada de clientes vinculados ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca de clientes realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<Page<ClienteFindResponseDto>> pesquisaCliente(
            @Parameter(description = "Filtro por nome") String nome,
            @Parameter(description = "Filtro por CPF") String cpf,
            @Parameter(description = "Filtro por telefone") String telefone,
            @Parameter(description = "Filtro por e-mail") String email,
            @Parameter(description = "Filtro por status do cliente") StatusClienteEnum status,
            @Parameter(description = "Número da página (inicia em 0)") Integer pagina,
            @Parameter(description = "Quantidade de registros por página") Integer tamanho
    );
}
