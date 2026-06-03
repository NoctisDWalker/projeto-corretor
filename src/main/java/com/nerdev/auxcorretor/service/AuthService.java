package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.auth.AuthResponseDTO;
import com.nerdev.auxcorretor.dto.auth.LoginRequestDTO;
import com.nerdev.auxcorretor.dto.auth.RegistrarCorretorRequestDTO;
import com.nerdev.auxcorretor.dto.auth.RegistrarCorretorResponseDTO;
import com.nerdev.auxcorretor.exception.AuthBussinessExeption;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.mapper.AuthMapper;
import com.nerdev.auxcorretor.model.*;
import com.nerdev.auxcorretor.model.enums.PapelCorretorContaEnum;
import com.nerdev.auxcorretor.model.enums.PerfilUsuarioEnum;
import com.nerdev.auxcorretor.model.enums.ProviderTypeEnum;
import com.nerdev.auxcorretor.repository.*;
import com.nerdev.auxcorretor.validation.AuthValidator;
import com.nerdev.auxcorretor.validation.RegistrarCorretorValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder encoder;
    private final AuthValidator authValidator;
    private final RegistrarCorretorValidator registrarCorretorValidator;

    private final CredencialUsuarioRepository credUserRepository;
    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CorretorRepository corretorRepository;
    private final CorretorContaRepository corretorContaRepository;

    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        CredencialUsuario credencial = credUserRepository.findByProviderTypeAndProviderUserId
                (loginRequestDTO.providerType(), loginRequestDTO.login()).orElseThrow(
                () -> new AuthBussinessExeption("Usuário ou senha inválidos"));

        boolean matchesPassword = encoder.matches(loginRequestDTO.senha(), credencial.getPasswordHash());

        if (!matchesPassword) {
            throw new AuthBussinessExeption("Usuário ou senha inválidos");
        }

        authValidator.validaLogin(credencial);
        CredencialUsuario credencialAtualizada = atualizaUltimoLogin(credencial);

        return AuthMapper.fromAuth(credencialAtualizada);
    }

    @Transactional
    public RegistrarCorretorResponseDTO registrarCorretor(RegistrarCorretorRequestDTO registrarCorretorRequestDTO, ProviderTypeEnum providerType) {
        registrarCorretorValidator.validarCadastro(registrarCorretorRequestDTO, providerType);

        Conta contaExistente = contaRepository.findById(registrarCorretorRequestDTO.contaId())
                .orElseThrow(() -> new BusinessException("Conta não encontrada."));

        Usuario usuario = criarUsuario(registrarCorretorRequestDTO, contaExistente);
        Corretor corretor = criarCorretor(registrarCorretorRequestDTO, usuario);

        String senhaHash = criptografarSenha(registrarCorretorRequestDTO.senha());
        CredencialUsuario credencialUsuario = criarCredencial(registrarCorretorRequestDTO, usuario, providerType, senhaHash);
        usuario.adicionarCredencial(credencialUsuario);

        CorretorConta corretorConta = criarCorretorConta(registrarCorretorRequestDTO, corretor, contaExistente);
        contaExistente.adicionarCorretorConta(corretorConta);

        salvarRegistro(usuario, corretor, corretorConta);

        return toResponse(registrarCorretorRequestDTO, usuario, corretor, credencialUsuario);
    }

    private void salvarRegistro(Usuario usuario, Corretor corretor, CorretorConta corretorConta) {
        usuarioRepository.save(usuario);
        corretorRepository.save(corretor);
        corretorContaRepository.save(corretorConta);
    }

    private RegistrarCorretorResponseDTO toResponse(RegistrarCorretorRequestDTO dto, Usuario usuario, Corretor corretor, CredencialUsuario credencial) {
        return new RegistrarCorretorResponseDTO(
                usuario.getId(),
                corretor.getId(),
                usuario.getNomeExibicao(),
                credencial.getProviderUserId()
        );
    }


    private CorretorConta criarCorretorConta(RegistrarCorretorRequestDTO dto, Corretor corretor, Conta conta) {
        return CorretorConta.builder()
                .corretor(corretor)
                .conta(conta)
                .papel(PapelCorretorContaEnum.CORRETOR)
                .emailProfissional(dto.email())
                .build();
    }

    private CredencialUsuario criarCredencial(RegistrarCorretorRequestDTO dto, Usuario usuario, ProviderTypeEnum providerType, String senha) {
        return CredencialUsuario.builder()
                .usuario(usuario)
                .providerType(providerType)
                .providerUserId(dto.login())
                .email(dto.email())
                .passwordHash(senha)
                .build();
    }

    private Corretor criarCorretor(RegistrarCorretorRequestDTO dto, Usuario usuario) {
        return Corretor.builder()
                .usuario(usuario)
                .nome(dto.nome())
                .cpf(dto.cpf())
                .bio(dto.bio())
                .creci(dto.creci())
                .build();
    }

    private Usuario criarUsuario(RegistrarCorretorRequestDTO dto, Conta conta) {
        return Usuario.builder()
                .conta(conta)
                .nomeExibicao(dto.nomeExibicao())
                .perfilUsuario(PerfilUsuarioEnum.CORRETOR)
                .build();
    }

    private CredencialUsuario atualizaUltimoLogin(CredencialUsuario credencial) {
        credencial.setLastLoginAt(LocalDateTime.now());
        return credUserRepository.save(credencial);
    }

    private String criptografarSenha(String senha) {
        return encoder.encode(senha);
    }

}
