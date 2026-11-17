package com.nerdev.auxcorretor.controller.docs;

import com.nerdev.auxcorretor.dto.imovel.ImovelCreateRequestDTO;
import com.nerdev.auxcorretor.dto.imovel.ImovelFindResponseDTO;
import com.nerdev.auxcorretor.dto.imovel.ImovelResponseDTO;
import com.nerdev.auxcorretor.dto.imovel.ImovelUpdateRequestDTO;
import com.nerdev.auxcorretor.model.enums.CompartilhamentoImovelEnum;
import com.nerdev.auxcorretor.model.enums.FinalidadeImovelEnum;
import com.nerdev.auxcorretor.model.enums.StatusImovelEnum;
import com.nerdev.auxcorretor.model.enums.TipoImovelEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static com.nerdev.auxcorretor.controller.docs.SwaggerConstants.*;

@Tag(name = "Imovel", description = "Gerenciamento da carteira de clientes")
@SecurityRequirement(name = "bearerAuth")
public interface ImoveisDoc {


    @Operation(summary = "Cadastrar imóvel", description = "Cadastra um novo imóvel vinculado ao corretor autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Imóvel cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada: campos obrigatórios ausentes ou status inicial diferente de DISPONIVEL.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<ImovelResponseDTO> salvar(ImovelCreateRequestDTO createDTO);

    @Operation(summary = "Atualizar imóvel", description = "Atualiza os dados de um imóvel pertencente ao corretor autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados do imóvel atualizados com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Imóvel não encontrado ou não pertence ao corretor autenticado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Transição de status inválida para o imóvel.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<ImovelResponseDTO> atualizar(
            @Parameter(description = "ID único do imóvel") UUID id,
            ImovelUpdateRequestDTO updateDTO
    );

    @Operation(summary = "Inativar imóvel", description = "Inativa logicamente um imóvel pertencente ao corretor autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Imóvel inativado com sucesso.", content = @Content),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Imóvel não encontrado ou não pertence ao corretor autenticado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Operação não permitida: o imóvel não pode ser inativado no status atual.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<Void> deletar(@Parameter(description = "ID único do imóvel") UUID id);

    @Operation(summary = "Reativar imóvel", description = "Reativa um imóvel inativo pertencente ao corretor autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Imóvel reativado com sucesso.", content = @Content),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Imóvel não encontrado ou não pertence ao corretor autenticado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Operação não permitida: o imóvel não pode ser reativado no status atual.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<Void> reativar(@Parameter(description = "ID único do imóvel") UUID id);

    @Operation(summary = "Buscar imóvel por ID", description = "Retorna os detalhes de um imóvel específico pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalhes do imóvel retornados com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "404", description = "Imóvel não encontrado.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Acesso restrito: o imóvel é privado ou já foi finalizado por outro corretor.", content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ImovelResponseDTO buscarPorId(@Parameter(description = "ID único do imóvel") UUID id);

    @Operation(summary = "Pesquisar todos os imóveis", description = "Pesquisa paginada de todos os imóveis disponíveis na rede/imobiliária.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listagem de imóveis retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = MSG_400, content = @Content),
            @ApiResponse(responseCode = "401", description = MSG_401, content = @Content),
            @ApiResponse(responseCode = "403", description = MSG_403, content = @Content),
            @ApiResponse(responseCode = "500", description = MSG_500, content = @Content)
    })
    ResponseEntity<Page<ImovelFindResponseDTO>> todos(
            @Parameter(description = "Filtro por título") String titulo,
            @Parameter(description = "Filtro por valor (mínimo desejado)") BigDecimal valorMinimo,
            @Parameter(description = "Filtro por valor (máximo desejado)") BigDecimal valorMaximo,
            @Parameter(description = "Filtro de imóvel por finalidade") FinalidadeImovelEnum finalidadeImovel,
            @Parameter(description = "Filtra imóvel por tipo") TipoImovelEnum tipoImovel,
            @Parameter(description = "Filtra imóvel por cidade") String cidade,
            @Parameter(description = "Filtra imóvel por bairro") String bairro,
            @Parameter(description = "Filtra imóvel por corretor") UUID corretorResponsavelId,
            @Parameter(description = "Filtra imóvel por data (mínimo desejado)") LocalDate dataCadastroInicial,
            @Parameter(description = "Filtra imóvel por data (máximo desejado)") LocalDate dataCadastroFinal,
            @Parameter(description = "Número da página (inicia em 0)") Integer pagina,
            @Parameter(description = "Quantidade de registros por página") Integer tamanho
    );


    ResponseEntity<Page<ImovelFindResponseDTO>> pesquisaMeusImoveis(
            @Parameter(description = "Filtro por título") String titulo,
            @Parameter(description = "Filtro por valor (mínimo desejado)") BigDecimal valorMinimo,
            @Parameter(description = "Filtro por valor (máximo desejado)") BigDecimal valorMaximo,
            @Parameter(description = "Filtro de imóvel por finalidade") FinalidadeImovelEnum finalidadeImovel,
            @Parameter(description = "Filtro se imóvel está compartilhado (ou não)") CompartilhamentoImovelEnum compartilhamentoImovel,
            @Parameter(description = "Filtra imóvel por tipo") TipoImovelEnum tipoImovel,
            @Parameter(description = "Filtra imóvel por cidade") String cidade,
            @Parameter(description = "Filtra imóvel por bairro") String bairro,
            @Parameter(description = "Filtra imóvel por status") StatusImovelEnum statusImovel,
            @Parameter(description = "Filtra imóvel por data (mínimo desejado)") LocalDate dataCadastroInicial,
            @Parameter(description = "Filtra imóvel por data (máximo desejado)") LocalDate dataCadastroFinal,
            @Parameter(description = "Número da página (inicia em 0)") Integer pagina,
            @Parameter(description = "Quantidade de registros por página") Integer tamanho
    );
}
