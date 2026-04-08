package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.ClienteCreateRequestDTO;
import com.nerdev.auxcorretor.dto.ClienteResponseDTO;
import com.nerdev.auxcorretor.dto.ClienteUpdateRequestDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.mapper.ClienteMapper;
import com.nerdev.auxcorretor.model.Cliente;
import com.nerdev.auxcorretor.model.enums.StatusClienteEnum;
import com.nerdev.auxcorretor.repository.ClienteRepository;
import com.nerdev.auxcorretor.validation.ClienteValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ClienteService {

    private ClienteRepository clienteRepository;
    private ClienteMapper clienteMapper;
    private ClienteValidator clienteValidator;

    public ClienteResponseDTO salvar(ClienteCreateRequestDTO dto){
        Cliente cliente = clienteMapper.toEntity(dto);
        clienteValidator.validaSalvar(cliente);
        cliente.setStatusCliente(StatusClienteEnum.ATIVO);
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return clienteMapper.toDto(clienteSalvo);
    }

    public ClienteResponseDTO atualizar(UUID id , ClienteUpdateRequestDTO dto){
        Cliente clienteEncontrado = clienteRepository.findById(id).orElseThrow(() -> new BusinessException("Cliente não Encontrado"));

        clienteMapper.updateEntity(clienteEncontrado, dto);
        clienteValidator.validaAtualizar(clienteEncontrado);
        Cliente clienteAtualizado = clienteRepository.save(clienteEncontrado);
        return clienteMapper.toDto(clienteAtualizado);
    }

    public void deletar(UUID id){
        Cliente clienteEncontrado = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não Encontrado"));

        clienteValidator.validaDeletar(clienteEncontrado);

        clienteEncontrado.setStatusCliente(StatusClienteEnum.INATIVO);
        clienteRepository.save(clienteEncontrado);
    }

    public ClienteResponseDTO reativarCliente(UUID id){
        Cliente clienteEncontrado = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não Encontrado"));
        if (clienteEncontrado.getStatusCliente() != StatusClienteEnum.INATIVO){
            throw new BusinessException("Somente clientes com status INATIVO podem ser reativados.");
        }

        clienteEncontrado.setStatusCliente(StatusClienteEnum.ATIVO);
        Cliente clienteSalvo = clienteRepository.save(clienteEncontrado);
        return clienteMapper.toDto(clienteSalvo);
    }
}
