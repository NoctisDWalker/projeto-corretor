package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.ClienteResponseDTO;
import com.nerdev.auxcorretor.dto.ClienteResquestDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.mapper.ClienteMapper;
import com.nerdev.auxcorretor.model.Cliente;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.model.enums.StatusClienteEnum;
import com.nerdev.auxcorretor.repository.ClienteRepository;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import com.nerdev.auxcorretor.validation.ClienteValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ClienteService {

    private ClienteRepository clienteRepository;
    private CorretorRepository corretorRepository;
    private ClienteMapper clienteMapper;
    private ClienteValidator clienteValidator;

    public ClienteResponseDTO salvar(ClienteResquestDTO dto){
        Corretor corretorEncontrado = corretorRepository.findById(dto.idCorretor())
                .orElseThrow(() -> new BusinessException("Corretor não encontrado"));

        Cliente cliente = clienteMapper.toEntity(dto);
        cliente.setCorretor(corretorEncontrado);
        clienteValidator.validaSalvar(cliente);
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return clienteMapper.toDto(clienteSalvo);
    }

    public ClienteResponseDTO atualizar(UUID id , ClienteResquestDTO dto){
        Cliente clienteEncontrado = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não Encontrado"));

        clienteMapper.updateEntity(clienteEncontrado, dto);

        clienteValidator.validaAtualizar(clienteEncontrado);

        Cliente clienteAtualizado = clienteRepository.save(clienteEncontrado);
        return clienteMapper.toDto(clienteAtualizado);
    }

    public void deletar(UUID id){
        Cliente clienteEncontrado = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não Encontrado"));

        clienteValidator.validaDeletar(clienteEncontrado);

        clienteEncontrado.setStatusCliente(StatusClienteEnum.PERDIDO);
        clienteRepository.save(clienteEncontrado);
    }

    public void reativarCliente(UUID id){
        Cliente clienteEncontrado = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não Encontrado"));
        if (clienteEncontrado.getStatusCliente() != StatusClienteEnum.PERDIDO){
            throw new BusinessException("Somente clientes com status PERDIDO podem ser reativados.");
        }

        clienteEncontrado.setStatusCliente(StatusClienteEnum.LEAD);
        clienteRepository.save(clienteEncontrado);
    }
}
