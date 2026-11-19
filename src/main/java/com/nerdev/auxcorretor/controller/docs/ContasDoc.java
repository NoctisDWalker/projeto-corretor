package com.nerdev.auxcorretor.controller.docs;

import com.nerdev.auxcorretor.dto.conta.ContaCreateRequestDTO;
import com.nerdev.auxcorretor.dto.conta.ContaResponseDTO;
import com.nerdev.auxcorretor.dto.conta.ContaUpdateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static com.nerdev.auxcorretor.controller.docs.SwaggerConstants.*;

@Tag(name = "Contas", description = "Gestão de contas.")
@SecurityRequirement(name = "bearerAuth")
public interface ContasDoc {

    @Operation(summary = "Cadastrar conta", description = "Cria uma nova conta no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada: nome, documento ou e-mail já em uso, ou status inicial inválido.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<ContaResponseDTO> salvarConta(ContaCreateRequestDTO dto);

    @Operation(summary = "Atualizar conta", description = "Atualiza os dados cadastrais da conta vinculada ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados da conta atualizados com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada ou não pertence ao escopo do usuário autenticado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada: conta expirada, com acesso bloqueado ou e-mail já utilizado por outra conta.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<ContaResponseDTO> atualizarConta(
            @Parameter(description = "ID único da conta") UUID id,
            ContaUpdateRequestDTO dto
    );

    @Operation(summary = "Cancelar conta", description = "Cancela definitivamente uma conta no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Conta cancelada com sucesso.", content = @Content),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Operação não permitida: o status atual da conta não permite cancelamento.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<Void> cancelarConta(@Parameter(description = "ID único da conta") UUID id);

    @Operation(summary = "Suspender conta", description = "Suspende temporariamente o acesso e as operações de uma conta.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Conta suspensa com sucesso.", content = @Content),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Operação não permitida: o status atual da conta não permite suspensão.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<Void> suspenderConta(@Parameter(description = "ID único da conta") UUID id);

}
