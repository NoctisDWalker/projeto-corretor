package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.corretor.CorretorResponseDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorUpdateRequestDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.mapper.CorretorMapper;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import com.nerdev.auxcorretor.validation.CorretorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CorretorService {

    private final CorretorRepository corretorRepository;
    private final CorretorMapper corretorMapper;
    private final CorretorValidator corretorValidator;


    public CorretorResponseDTO atualizar(UUID id, CorretorUpdateRequestDTO dto){
        // todo: obter autenticação do usuario logado

        Corretor corretorEncontrado = corretorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Corretor não encontrado"));

        corretorValidator.validarAtualizar(corretorEncontrado);
        corretorMapper.updateEntity(corretorEncontrado, dto);

        Corretor corretorSalvo = corretorRepository.save(corretorEncontrado);

        return corretorMapper.toResponseDTO(corretorSalvo);
    }

    public void deletar(UUID id){
        // todo: obter autenticação do usuario logado

        Corretor corretor = corretorRepository.findById(id)
                        .orElseThrow(() -> new BusinessException("Corretor não encontrado"));

        corretorValidator.validarDeletar(corretor);
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