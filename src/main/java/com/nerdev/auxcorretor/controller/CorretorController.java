package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.controller.docs.CorretoresDoc;
import com.nerdev.auxcorretor.dto.corretor.CorretorFindResponseDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorResponseDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorUpdateRequestDTO;
import com.nerdev.auxcorretor.security.annotations.permissions.corretores.*;
import com.nerdev.auxcorretor.service.CorretorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/corretores")
@RequiredArgsConstructor
public class CorretorController implements CorretoresDoc {

    private final CorretorService corretorService;

    @Override
    @PutMapping("/{id}")
    @PodeAtualizarCorretores
    public ResponseEntity<CorretorResponseDTO> atualizar(
            @PathVariable UUID id, @RequestBody @Valid CorretorUpdateRequestDTO dto) {

        CorretorResponseDTO corretorAtualizado = corretorService.atualizar(id, dto);
        return ResponseEntity.ok(corretorAtualizado);
    }
    @Override
    @DeleteMapping("/{id}")
    @PodeDeletarCorretores
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {

        corretorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    @Override
    @PatchMapping("/{id}/reativar")
    @PodeReativarCorretores
    public ResponseEntity<Void> reativar(@PathVariable UUID id) {
        corretorService.reativarCorretor(id);
        return ResponseEntity.noContent().build();
    }
    @Override
    @GetMapping("/{id}/buscarUm")
    @PodeBuscarCorretoresPorId
    public ResponseEntity<CorretorResponseDTO> buscarCorretorPorId(@PathVariable UUID id) {
        CorretorResponseDTO corretorResponseDTO = corretorService.buscarCorretorPorId(id);
        return ResponseEntity.ok(corretorResponseDTO);
    }
    @Override
    @GetMapping("/buscarTodos")
    @PodeBuscarTodosOsCorretores
    public ResponseEntity<List<CorretorResponseDTO>> buscarTodos() {
        List<CorretorResponseDTO> corretores = corretorService.listarCorretores();
        return ResponseEntity.ok(corretores);
    }
    @Override
    @GetMapping("/pesquisa")
    @PodePesquisarCorretores
    public ResponseEntity<Page<CorretorFindResponseDTO>> pesquisaCorretores(
            @RequestParam(value = "id", required = false) UUID id,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "cpf", required = false) String cpf,
            @RequestParam(value = "creci", required = false) String creci,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho

    ) {
        Page<CorretorFindResponseDTO> resultado = corretorService.pesquisaCorretores(id, nome, cpf, creci, pagina, tamanho);
        return ResponseEntity.ok(resultado);
    }


}
