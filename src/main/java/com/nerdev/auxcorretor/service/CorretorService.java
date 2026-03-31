package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.CorretorCreateRequestDTO;
import com.nerdev.auxcorretor.dto.CorretorResponseDTO;
import com.nerdev.auxcorretor.dto.CorretorUpdateRequestDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.mapper.CorretorMapper;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import com.nerdev.auxcorretor.validation.CorretorValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
public class CorretorService {

    private final CorretorRepository corretorRepository;
    private final PasswordEncoder encoder;
    private final CorretorMapper corretorMapper;
    private final CorretorValidator corretorValidator;

    public CorretorResponseDTO salvar(CorretorCreateRequestDTO dto){
        // todo: obter autenticação do usuario logado

        Corretor corretor = corretorMapper.toEntity(dto);

        corretorValidator.validarCadastro(corretor);
        criptografarSenhaSeNescessario(corretor);
        Corretor corretorSalvo = corretorRepository.save(corretor);

        return corretorMapper.toResponseDTO(corretorSalvo);
    }

    public CorretorResponseDTO atualizar(UUID id, CorretorUpdateRequestDTO dto){
        // todo: obter autenticação do usuario logado

        Corretor corretorEncontrado = corretorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Corretor não encontrado"));

        corretorMapper.updateEntity(corretorEncontrado, dto);
        corretorValidator.validarAtualizar(corretorEncontrado);
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

    private void criptografarSenhaSeNescessario(Corretor corretor){
        String senha = corretor.getSenha();
        if (!senha.startsWith("{")){
            corretor.setSenha(encoder.encode(senha));
        }
    }

}