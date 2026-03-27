package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CorretorService {

    private final CorretorRepository corretorRepository;

    public Corretor salvar(Corretor corretor){
        // todo: adicionar validator
        // todo: implementar dtos e mappers
        // todo: adicionar encoder para senha
        // todo: obter atenticação do usuario logado
        if(corretorRepository.existsByEmail(corretor.getEmail())){
            throw new IllegalArgumentException("Email já cadastrado!");
        }
        return corretorRepository.save(corretor);
    }

    public void atualizar(Corretor corretor){
        // todo: adicionar validator
        // todo: implementar dtos e mappers
        // todo: adicionar encoder para senha
        // todo: obter atenticação do usuario logado
        if (corretor.getId() == null){
            throw new IllegalArgumentException("Para atualizar, é necessario que o corretor já esteja salvo na base");
        }

        if (!corretorRepository.existsById(corretor.getId())){
            throw new IllegalArgumentException("Corretor não encontrado para atualizar");
        }
        corretorRepository.save(corretor);
    }

    public void deletar(Corretor corretor){
        // todo: implementar dtos e mappers
        if (!corretorRepository.existsById(corretor.getId())){
            throw new IllegalArgumentException("Corretor não encontrado para deletar");
        }
        corretor.setAtivo(false);
        corretorRepository.save(corretor);
    }
}