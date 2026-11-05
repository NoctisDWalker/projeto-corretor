package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.cliente.ClienteCreateRequestDTO;
import com.nerdev.auxcorretor.dto.cliente.ClienteFindResponseDto;
import com.nerdev.auxcorretor.dto.cliente.ClienteResponseDTO;
import com.nerdev.auxcorretor.dto.cliente.ClienteUpdateRequestDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.exception.NotFoundException;
import com.nerdev.auxcorretor.mapper.ClienteMapper;
import com.nerdev.auxcorretor.model.Cliente;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.model.enums.StatusClienteEnum;
import com.nerdev.auxcorretor.repository.ClienteRepository;
import com.nerdev.auxcorretor.repository.specs.ClienteSpecs;
import com.nerdev.auxcorretor.repository.specs.SpecificationBuilder;
import com.nerdev.auxcorretor.validation.ClienteValidator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ClienteService {

    private ClienteRepository clienteRepository;
    private ClienteMapper clienteMapper;
    private ClienteValidator clienteValidator;
    private ServiceUtils serviceUtils;

    public ClienteResponseDTO salvar(ClienteCreateRequestDTO dto){
        Corretor corretor = getCorretorLogado();
        Cliente cliente = clienteMapper.toEntity(dto);
        prepararNovoCliente(cliente, corretor);
        clienteValidator.validaSalvar(cliente, corretor.getId());

        Cliente clienteSalvo = clienteRepository.save(cliente);
        return clienteMapper.toDto(clienteSalvo);
    }

    public ClienteResponseDTO atualizar(UUID id , ClienteUpdateRequestDTO dto){
        Corretor corretorLogado = getCorretorLogado();
        Cliente clienteEncontrado = clienteRepository.findByIdAndCorretorId(id, corretorLogado.getId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        clienteMapper.updateEntity(clienteEncontrado, dto);

        clienteValidator.validaAtualizar(clienteEncontrado, corretorLogado.getId());
        Cliente clienteAtualizado = clienteRepository.save(clienteEncontrado);
        return clienteMapper.toDto(clienteAtualizado);
    }

    public void deletar(UUID id){
        Corretor corretorLogado = getCorretorLogado();
        Cliente clienteEncontrado = clienteRepository.findByIdAndCorretorId(id, corretorLogado.getId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        clienteValidator.validaDeletar(clienteEncontrado);

        clienteEncontrado.setStatusCliente(StatusClienteEnum.INATIVO);
        clienteRepository.save(clienteEncontrado);
    }

    public ClienteResponseDTO reativarCliente(UUID id){
        Corretor corretorLogado = getCorretorLogado();
        Cliente clienteEncontrado = clienteRepository.findByIdAndCorretorId(id, corretorLogado.getId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        if (clienteEncontrado.getStatusCliente() != StatusClienteEnum.INATIVO){
            throw new BusinessException("Somente clientes com status INATIVO podem ser reativados.");
        }

        clienteEncontrado.setStatusCliente(StatusClienteEnum.ATIVO);
        Cliente clienteSalvo = clienteRepository.save(clienteEncontrado);
        return clienteMapper.toDto(clienteSalvo);
    }

    public Page<ClienteFindResponseDto> pesquisaCliente(
            String nome,
            String cpf,
            String telefone,
            String email,
            StatusClienteEnum status,
            Integer pagina,
            Integer tamanho
    ){

        Corretor corretorLogado = getCorretorLogado();

        Specification<Cliente> spec = new SpecificationBuilder<Cliente>()
                .and(corretorLogado.getId(), ClienteSpecs::idCorretorEquals)
                .andIfNotBlank(nome, ClienteSpecs::nomeLike)
                .andIfNotBlank(cpf, ClienteSpecs::cpfLike)
                .andIfNotBlank(telefone, ClienteSpecs::telefoneLike)
                .andIfNotBlank(email, ClienteSpecs::emailLike)
                .and(status, ClienteSpecs::statusEquals)
                .build();


        Pageable pageable = PageRequest.of(pagina, tamanho);
        Page<Cliente> page = clienteRepository.findAll(spec, pageable);

        return page.map(clienteMapper::toFindDto);
    }


    private Corretor getCorretorLogado() {
        return serviceUtils.obterCorretorLogado();
    }

    private void prepararNovoCliente(Cliente cliente, Corretor corretor) {
        cliente.setStatusCliente(StatusClienteEnum.ATIVO);
        cliente.setCorretor(corretor);
    }
}
