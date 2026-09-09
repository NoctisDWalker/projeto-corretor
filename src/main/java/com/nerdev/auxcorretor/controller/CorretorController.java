package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.dto.corretor.CorretorFindResponseDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorResponseDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorUpdateRequestDTO;
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
public class CorretorController {

    private final CorretorService corretorService;


    @PutMapping("/{id}")
    public ResponseEntity<CorretorResponseDTO> atualizar(
            @PathVariable UUID id, @RequestBody @Valid CorretorUpdateRequestDTO dto) {

        CorretorResponseDTO corretorAtualizado = corretorService.atualizar(id, dto);
        return ResponseEntity.ok(corretorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {

        corretorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable UUID id) {
        corretorService.reativarCorretor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/buscarUm")
    public ResponseEntity<CorretorResponseDTO> buscarCorretorPorId(@PathVariable UUID id) {
        CorretorResponseDTO corretorResponseDTO = corretorService.buscarCorretorPorId(id);
        return ResponseEntity.ok(corretorResponseDTO);
    }

    @GetMapping("/buscarTodos")
    public ResponseEntity<List<CorretorResponseDTO>> buscarTodos() {
        List<CorretorResponseDTO> corretores = corretorService.listarCorretores();
        return ResponseEntity.ok(corretores);
    }

    @GetMapping("/pesquisa")
    ResponseEntity<Page<CorretorFindResponseDTO>> pesquisaCorretores(
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
