package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.dto.ClienteResponseDTO;
import com.nerdev.auxcorretor.dto.ClienteResquestDTO;
import com.nerdev.auxcorretor.service.ClienteService;
import com.nerdev.auxcorretor.web.util.RestLocationBuilder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
@AllArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final RestLocationBuilder locationBuilder;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> salvar(@RequestBody @Valid ClienteResquestDTO dto){
        ClienteResponseDTO cliente = clienteService.salvar(dto);
        URI location = locationBuilder.build(cliente.id());
        return ResponseEntity.created(location).body(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar (@PathVariable UUID id, @RequestBody @Valid ClienteResquestDTO dto){
        ClienteResponseDTO clienteAtualizado = clienteService.atualizar(id, dto);
        return ResponseEntity.ok(clienteAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id){
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable UUID id){
        clienteService.reativarCliente(id);
        return ResponseEntity.noContent().build();
    }

}
