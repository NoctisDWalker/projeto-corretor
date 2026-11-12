package com.nerdev.auxcorretor.dto.imovel;

import com.nerdev.auxcorretor.dto.corretor.CorretorResumoDTO;
import com.nerdev.auxcorretor.model.enums.CompartilhamentoImovelEnum;
import com.nerdev.auxcorretor.model.enums.FinalidadeImovelEnum;
import com.nerdev.auxcorretor.model.enums.StatusImovelEnum;
import com.nerdev.auxcorretor.model.enums.TipoImovelEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema
public record ImovelFindResponseDTO(
        UUID id,
        String titulo,
        String descricao,
        BigDecimal valor,
        FinalidadeImovelEnum finalidadeImovel,
        TipoImovelEnum tipoImovel,
        String cidade,
        String bairro,
        StatusImovelEnum statusImovel,
        CorretorResumoDTO corretor,
        CompartilhamentoImovelEnum compartilhamentoImovel,
        int quantidadeVisitas,
        LocalDate dataCadastro,
        LocalDate dataAtualizacao
) {
}
