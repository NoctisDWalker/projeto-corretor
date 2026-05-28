package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.corretor.CorretorCreateRequestDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorResponseDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorUpdateRequestDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.mapper.CorretorMapper;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import com.nerdev.auxcorretor.validation.CorretorValidator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CorretorService {

    private final CorretorRepository corretorRepository;
    private final CorretorMapper corretorMapper;
    private final CorretorValidator corretorValidator;

    public CorretorResponseDTO salvar(CorretorCreateRequestDTO dto){
        // todo: obter autenticação do usuario logado

        Corretor corretor = corretorMapper.toEntity(dto);

        corretorValidator.validarCadastro(corretor);
        Corretor corretorSalvo = corretorRepository.save(corretor);

        return corretorMapper.toResponseDTO(corretorSalvo);
    }

    public CorretorResponseDTO atualizar(UUID id, CorretorUpdateRequestDTO dto){
        // todo: obter autenticação do usuario logado

        Corretor corretorEncontrado = corretorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Corretor não encontrado"));

        String cpf = corretorEncontrado.getCpf();
        corretorMapper.updateEntity(corretorEncontrado, dto);
        corretorValidator.validarAtualizar(corretorEncontrado);
        if(!corretorEncontrado.getCpf().equals(cpf)){
            throw new BusinessException("CPF não pode ser alterado");
        }
        Corretor corretorSalvo = corretorRepository.save(corretorEncontrado);
        return corretorMapper.toResponseDTO(corretorSalvo);
    }

    public void deletar(UUID id){
        // todo: obter autenticação do usuario logado

        Corretor corretor = corretorRepository.findById(id)
                        .orElseThrow(() -> new BusinessException("Corretor não encontrado"));

        corretorValidator.validarDeletar(id);
        corretor.setAtivo(false);
        corretorRepository.save(corretor);
    }

    public void reativarCorretor(UUID id){
        Corretor corretorEncontrado = corretorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Corretor não encontrado"));
        if(corretorEncontrado.isAtivo()){
           throw new BusinessException("Somente corretores inativos podem ser reativados");
        }
        corretorEncontrado.setAtivo(true);
        corretorRepository.save(corretorEncontrado);
    }
}