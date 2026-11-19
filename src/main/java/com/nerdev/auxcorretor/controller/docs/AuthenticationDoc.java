package com.nerdev.auxcorretor.controller.docs;

import com.nerdev.auxcorretor.dto.auth.RegistrarCorretorRequestDTO;
import com.nerdev.auxcorretor.dto.auth.RegistrarCorretorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import static com.nerdev.auxcorretor.controller.docs.SwaggerConstants.*;

@Tag(name = "Autenticação", description = "Registros de Novos Usuários e Processos de Autenticação")
@SecurityRequirement(name = "bearerAuth")
public interface AuthenticationDoc {

    @Operation(summary = "Registrar corretor individual", description = "Realiza o auto-cadastro de um corretor individual criando sua conta no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Corretor registrado com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada: conta de origem não encontrada ou cancelada, ou dados únicos (login, e-mail, CPF, CRECI) já em uso no sistema.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<RegistrarCorretorResponseDTO> registrarCorretorIndividual(
            RegistrarCorretorRequestDTO registrarDto
    );

}
