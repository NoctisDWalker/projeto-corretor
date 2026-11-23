package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.controller.docs.ClientesDoc;
import com.nerdev.auxcorretor.dto.cliente.ClienteCreateRequestDTO;
import com.nerdev.auxcorretor.dto.cliente.ClienteFindResponseDto;
import com.nerdev.auxcorretor.dto.cliente.ClienteResponseDTO;
import com.nerdev.auxcorretor.dto.cliente.ClienteUpdateRequestDTO;
import com.nerdev.auxcorretor.model.enums.StatusClienteEnum;
import com.nerdev.auxcorretor.security.annotations.permissions.clientes.*;
import com.nerdev.auxcorretor.service.ClienteService;
import com.nerdev.auxcorretor.web.util.RestLocationBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController implements ClientesDoc {

    private final ClienteService clienteService;
    private final RestLocationBuilder locationBuilder;

    @Override
    @PostMapping
    @PodeCadastrarCliente
    public ResponseEntity<ClienteResponseDTO> salvar(@RequestBody @Valid ClienteCreateRequestDTO dto) {
        ClienteResponseDTO cliente = clienteService.salvar(dto);
        log.info("Salvando novo cliente {}", cliente.id());
        URI location = locationBuilder.build(cliente.id());
        return ResponseEntity.created(location).body(cliente);
    }

    @Override
    @PutMapping("/{id}")
    @PodeAtualizarCliente
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable UUID id, @RequestBody @Valid ClienteUpdateRequestDTO dto) {
        log.info("Atualizando cliente {}", dto.nome());
        ClienteResponseDTO clienteAtualizado = clienteService.atualizar(id, dto);
        return ResponseEntity.ok(clienteAtualizado);
    }

    @Override
    @DeleteMapping("/{id}")
    @PodeDeletarCliente
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/reativar")
    @PodeReativarCliente
    public ResponseEntity<ClienteResponseDTO> reativar(@PathVariable UUID id) {
        ClienteResponseDTO clienteSalvo = clienteService.reativarCliente(id);
        return ResponseEntity.ok(clienteSalvo);
    }

    @Override
    @GetMapping("/pesquisa")
    @PodePesquisarCliente
    public ResponseEntity<Page<ClienteFindResponseDto>> pesquisaCliente(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "cpf", required = false) String cpf,
            @RequestParam(value = "telefone", required = false) String telefone,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "status", required = false) StatusClienteEnum status,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamanho", defaultValue = "10") Integer tamanho
    ) {
        Page<ClienteFindResponseDto> resultado = clienteService.pesquisaCliente(nome, cpf, telefone, email, status, pagina, tamanho);
        return ResponseEntity.ok(resultado);
    }
}
