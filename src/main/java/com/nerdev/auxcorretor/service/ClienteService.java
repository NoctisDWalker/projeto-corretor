package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.ClienteResponseDTO;
import com.nerdev.auxcorretor.dto.ClienteResquestDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.mapper.ClienteMapper;
import com.nerdev.auxcorretor.model.Cliente;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.model.StatusClienteEnum;
import com.nerdev.auxcorretor.repository.ClienteRepository;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ClienteService {

    private ClienteRepository clienteRepository;
    private CorretorRepository corretorRepository;
    private ClienteMapper clienteMapper;


    public ClienteResponseDTO salvar(ClienteResquestDTO dto){
        Corretor corretorEncontrado = corretorRepository.findById(dto.idCorretor())
                .orElseThrow(() -> new BusinessException("Corretor não encontrado"));

        // Todo: validar

        Cliente cliente = clienteMapper.toEntity(dto);
        cliente.setCorretor(corretorEncontrado);
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return clienteMapper.toDto(clienteSalvo);
    }

   /* public ClienteResponseDTO atualizar(UUID id , ClienteResquestDTO dto){
        Cliente clienteEncontrado = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não Encontrado"));

        clienteMapper.updateEntity(clienteEncontrado, dto);

        // Todo: validar

        Cliente clienteAtualizado = clienteRepository.save(clienteEncontrado);
        return clienteMapper.toDto(clienteAtualizado);
    }*/

    public void deletar(UUID id){
        Cliente clienteEncontrado = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não Encontrado"));

        // Validar

        clienteEncontrado.setStatusCliente(StatusClienteEnum.PERDIDO);
        clienteRepository.save(clienteEncontrado);
    }

}
