package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.dto.ImovelCreateRequestDTO;
import com.nerdev.auxcorretor.dto.ImovelResponseDTO;
import com.nerdev.auxcorretor.dto.ImovelUpdateRequestDTO;
import com.nerdev.auxcorretor.service.ImovelService;
import com.nerdev.auxcorretor.web.util.RestLocationBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/imoveis")
@RequiredArgsConstructor
public class ImovelController {

    private final ImovelService imovelService;
    private final RestLocationBuilder restLocationBuilder;

    @PostMapping
    public ResponseEntity<ImovelResponseDTO> salvar(@RequestBody @Valid ImovelCreateRequestDTO createDTO) {
        ImovelResponseDTO salvo = imovelService.salvar(createDTO);
        URI location = restLocationBuilder.build(salvo.id());
        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImovelResponseDTO> atualizar(@PathVariable UUID id, @RequestBody @Valid ImovelUpdateRequestDTO updateDTO) {
        ImovelResponseDTO atualizado = imovelService.atualizar(id, updateDTO);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        imovelService.inativar(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable UUID id) {
        imovelService.reativar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ImovelResponseDTO buscarPorId(@PathVariable UUID id) {
        return imovelService.buscarPorId(id);
    }

    @GetMapping("/meus")
    public List<ImovelResponseDTO> buscarMeusImoveis() {
        return imovelService.buscarMeusImoveis();
    }

    @GetMapping("/todos")
    public List<ImovelResponseDTO> buscarTodos() {
        return imovelService.listarTodosImoveis();
    }
}
