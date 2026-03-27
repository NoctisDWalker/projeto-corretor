package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
public class CorretorService {

    private final CorretorRepository corretorRepository;
    private final PasswordEncoder encoder;

    public Corretor salvar(Corretor corretor){
        // todo: adicionar validator
        // todo: implementar dtos e mappers
        // todo: adicionar encoder para senha
        // todo: obter atenticação do usuario logado
        if(corretorRepository.existsByEmail(corretor.getEmail())){
            throw new IllegalArgumentException("Email já cadastrado!");
        }
        criptografarSenhaSeNescessario(corretor);
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
        criptografarSenhaSeNescessario(corretor);
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

    private void criptografarSenhaSeNescessario(Corretor corretor){
        String senha = corretor.getSenha();
        if (senha != null && senha.startsWith("{")){
            corretor.setSenha(encoder.encode(senha));
        }
    }

}