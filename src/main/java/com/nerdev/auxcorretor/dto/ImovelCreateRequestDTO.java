package com.nerdev.auxcorretor.dto;

import com.nerdev.auxcorretor.model.enums.FinalidadeImovelEnum;
import com.nerdev.auxcorretor.model.enums.TipoImovelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema
public record ImovelCreateRequestDTO(
        @NotBlank String titulo,
        String descricao,
        @NotNull BigDecimal valor,
        @NotNull FinalidadeImovelEnum finalidadeImovel,
        @NotNull TipoImovelEnum tipoImovel,
        String cidade,
        String bairro
) {
}
