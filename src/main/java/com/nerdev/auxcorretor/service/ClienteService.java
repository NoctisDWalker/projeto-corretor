package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.cliente.ClienteCreateRequestDTO;
import com.nerdev.auxcorretor.dto.cliente.ClienteFindResponseDto;
import com.nerdev.auxcorretor.dto.cliente.ClienteResponseDTO;
import com.nerdev.auxcorretor.dto.cliente.ClienteUpdateRequestDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.mapper.ClienteMapper;
import com.nerdev.auxcorretor.model.Cliente;
import com.nerdev.auxcorretor.model.enums.StatusClienteEnum;
import com.nerdev.auxcorretor.repository.ClienteRepository;
import com.nerdev.auxcorretor.validation.ClienteValidator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import static com.nerdev.auxcorretor.repository.specs.ClienteSpecs.*;

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

    public Page<ClienteFindResponseDto> pesquisaCliente(
            String nome,
            String cpf,
            String telefone,
            String email,
            StatusClienteEnum status,
            Integer pagina,
            Integer tamanho
    ){

        Specification<Cliente> spec = (root, query, cb) -> cb.conjunction();

        if (isNotNullOrEmpty(nome)) {
            spec = spec.and(nomeLike(nome));
        }
        if (isNotNullOrEmpty(cpf)) {
            spec = spec.and(cpfLike(cpf));
        }
        if (isNotNullOrEmpty(telefone)) {
            spec = spec.and(telefoneLike(telefone));
        }
        if (isNotNullOrEmpty(email)) {
            spec = spec.and(emailLike(email));
        }
        if (status != null) {
            spec = spec.and(statusEquals(status));
        }

        Pageable pageable = PageRequest.of(pagina, tamanho);
        Page<Cliente> page = clienteRepository.findAll(spec, pageable);

        return page.map(clienteMapper::toFindDto);
    }

    private boolean isNotNullOrEmpty(String string){
        return string != null && !string.isEmpty();
    }

}
