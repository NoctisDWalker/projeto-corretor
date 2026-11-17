package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.controller.docs.ImoveisDoc;
import com.nerdev.auxcorretor.dto.imovel.ImovelCreateRequestDTO;
import com.nerdev.auxcorretor.dto.imovel.ImovelFindResponseDTO;
import com.nerdev.auxcorretor.dto.imovel.ImovelResponseDTO;
import com.nerdev.auxcorretor.dto.imovel.ImovelUpdateRequestDTO;
import com.nerdev.auxcorretor.model.enums.CompartilhamentoImovelEnum;
import com.nerdev.auxcorretor.model.enums.FinalidadeImovelEnum;
import com.nerdev.auxcorretor.model.enums.StatusImovelEnum;
import com.nerdev.auxcorretor.model.enums.TipoImovelEnum;
import com.nerdev.auxcorretor.security.annotations.permissions.imoveis.*;
import com.nerdev.auxcorretor.service.ImovelService;
import com.nerdev.auxcorretor.web.util.RestLocationBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/imoveis")
@RequiredArgsConstructor
public class ImovelController implements ImoveisDoc {

    private final ImovelService imovelService;
    private final RestLocationBuilder restLocationBuilder;

    @Override
    @PostMapping
    @PodeCadastrarImoveis
    public ResponseEntity<ImovelResponseDTO> salvar(@RequestBody @Valid ImovelCreateRequestDTO createDTO) {
        ImovelResponseDTO salvo = imovelService.salvar(createDTO);
        URI location = restLocationBuilder.build(salvo.id());
        return ResponseEntity.created(location).body(salvo);
    }

    @Override
    @PutMapping("/{id}")
    @PodeAtualizarImoveis
    public ResponseEntity<ImovelResponseDTO> atualizar(@PathVariable UUID id, @RequestBody @Valid ImovelUpdateRequestDTO updateDTO) {
        ImovelResponseDTO atualizado = imovelService.atualizar(id, updateDTO);
        return ResponseEntity.ok(atualizado);
    }

    @Override
    @DeleteMapping("/{id}")
    @PodeDeletarImoveis
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        imovelService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/reativar")
    @PodeReativarImoveis
    public ResponseEntity<Void> reativar(@PathVariable UUID id) {
        imovelService.reativar(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{id}")
    @PodeBuscarImovelPorId
    public ImovelResponseDTO buscarPorId(@PathVariable UUID id) {
        return imovelService.buscarPorId(id);
    }

    @Override
    @GetMapping("/todos")
    @PodeBuscarTodosOsImoveis
    public ResponseEntity<Page<ImovelFindResponseDTO>> todos(
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "valor-minimo", required = false) BigDecimal valorMinimo,
            @RequestParam(value = "valor-maximo", required = false) BigDecimal valorMaximo,
            @RequestParam(value = "finalidade-imovel", required = false) FinalidadeImovelEnum finalidadeImovel,
            @RequestParam(value = "tipo-imovel", required = false) TipoImovelEnum tipoImovel,
            @RequestParam(value = "cidade", required = false) String cidade,
            @RequestParam(value = "bairro", required = false) String bairro,
            @RequestParam(value = "corretor-responsavel-id", required = false) UUID corretorResponsavelId,
            @RequestParam(value = "data-cadastro-inicial", required = false) LocalDate dataCadastroInicial,
            @RequestParam(value = "data-cadastro-final", required = false) LocalDate dataCadastroFinal,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho
    ) {
        Page<ImovelFindResponseDTO> resultado = imovelService.listarTodosImoveis(
                titulo, valorMinimo, valorMaximo, finalidadeImovel, tipoImovel, cidade,
                bairro, corretorResponsavelId, dataCadastroInicial, dataCadastroFinal, pagina, tamanho
        );
        return ResponseEntity.ok(resultado);
    }

    @Override
    @GetMapping("/meus")
    @PodeBuscarMeusImoveis
    public ResponseEntity<Page<ImovelFindResponseDTO>> pesquisaMeusImoveis(
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "valor-minimo", required = false) BigDecimal valorMinimo,
            @RequestParam(value = "valor-maximo", required = false) BigDecimal valorMaximo,
            @RequestParam(value = "finalidade-imovel", required = false) FinalidadeImovelEnum finalidadeImovel,
            @RequestParam(value = "compartilhamento-imovel", required = false) CompartilhamentoImovelEnum compartilhamentoImovel,
            @RequestParam(value = "tipo-imovel", required = false) TipoImovelEnum tipoImovel,
            @RequestParam(value = "cidade", required = false) String cidade,
            @RequestParam(value = "bairro", required = false) String bairro,
            @RequestParam(value = "status-imovel", required = false) StatusImovelEnum statusImovel,
            @RequestParam(value = "data-cadastro-inicial", required = false) LocalDate dataCadastroInicial,
            @RequestParam(value = "data-cadastro-final", required = false) LocalDate dataCadastroFinal,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho
    ) {
        Page<ImovelFindResponseDTO> resultado = imovelService.pesquisaMeusImoveis(
                titulo, valorMinimo, valorMaximo, finalidadeImovel, compartilhamentoImovel, tipoImovel, cidade, bairro,
                statusImovel, dataCadastroInicial, dataCadastroFinal, pagina, tamanho
        );
        return ResponseEntity.ok(resultado);
    }
}
