package com.nerdev.auxcorretor.controller.docs;

import com.nerdev.auxcorretor.dto.ClientApi.ClientApiRequestDTO;
import com.nerdev.auxcorretor.dto.ClientApi.ClientApiResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import static com.nerdev.auxcorretor.controller.docs.SwaggerConstants.*;

@Tag(name = "ClientApi", description = "Gerenciamento de clients para integrações externas.")
@SecurityRequirement(name = "bearerAuth")
public interface ClientApiDoc {

    @Operation(summary = "Cadastrar client", description = "Cadastra um novo client ID e secret para comunicação via credenciais OAuth2.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client OAuth2 cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Requisição inválida: estrutura do JSON malformada (o DTO não possui validações de campos).", content = @Content),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada: já existe um client registrado com o ID informado.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<ClientApiResponseDTO> Cadastrar(ClientApiRequestDTO dto);

}
