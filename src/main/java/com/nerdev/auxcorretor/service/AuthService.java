package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.auth.*;
import com.nerdev.auxcorretor.dto.conta.ContaCreateRequestDTO;
import com.nerdev.auxcorretor.dto.conta.ContaResponseDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorCreateRequestDTO;
import com.nerdev.auxcorretor.dto.imobiliaria.ImobiliariaCreateRequestDto;
import com.nerdev.auxcorretor.dto.imobiliaria.endereco.EnderecoCreateRequestDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.mapper.AuthMapper;
import com.nerdev.auxcorretor.model.*;
import com.nerdev.auxcorretor.model.enums.PerfilUsuarioEnum;
import com.nerdev.auxcorretor.model.enums.ProviderTypeEnum;
import com.nerdev.auxcorretor.repository.*;
import com.nerdev.auxcorretor.validation.RegistrarCorretorValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder encoder;
    private final RegistrarCorretorValidator registrarCorretorValidator;
    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CorretorRepository corretorRepository;
    private final CorretorContaRepository corretorContaRepository;
    private final ImobiliariaRepository imobiliariaRepository;
    private final ContaService contaService;
    private final AuthMapper authMapper;


    @Transactional
    public RegistrarCorretorResponseDTO registrarCorretorIndividual(RegistrarCorretorRequestDTO createRequest) {

        ContaCreateRequestDTO contaRequest = createRequest.conta();
        CorretorCreateRequestDTO corretorRequest = createRequest.corretor();
        UsuarioCredencialRequestDTO userCredResquestDTO = createRequest.credencialUsuario();

        ContaResponseDTO contaResponse = contaService.criarConta(contaRequest);
        Conta conta = contaRepository.getReferenceById(contaResponse.contaId());

        Usuario usuario = criarUsuario(userCredResquestDTO.nomeExibicao(), conta, PerfilUsuarioEnum.ROLE_CORRETOR_INDIVIDUAL);
        String senhaHash = criptografarSenha(userCredResquestDTO.senha());
        CredencialUsuario credencialUsuario = criarCredencial(
                userCredResquestDTO.login(),
                userCredResquestDTO.email(),
                usuario,
                ProviderTypeEnum.LOGIN_LOCAL,
                senhaHash);
        usuario.adicionarCredencial(credencialUsuario);

        Corretor corretor = criarCorretor(corretorRequest, usuario);

        registrarCorretorValidator.validarCadastro(corretor, credencialUsuario, conta);

        CorretorConta corretorConta = criarCorretorConta(corretor, conta);
        corretor.setCorretorContas(Set.of(corretorConta));

        corretorRepository.save(corretor);
        usuarioRepository.save(usuario);
        corretorContaRepository.save(corretorConta);

        return authMapper.responseCorretor(contaResponse, corretor, usuario);
    }

    @Transactional
    public RegistrarImobiliariaResponseDTO registrarImobiliaria(RegistrarImobiliariaRequestDTO createRequest) {

        ContaCreateRequestDTO contaRequestDTO = createRequest.conta();
        ImobiliariaCreateRequestDto imobiliariaRequestDTO = createRequest.imobiliaria();
        EnderecoCreateRequestDTO enderecoRequestDTO = createRequest.endereco();
        UsuarioCredencialRequestDTO userCredResquestDTO = createRequest.credencialUsuario();

        ContaResponseDTO contaResponse = contaService.criarConta(contaRequestDTO);
        Conta conta = contaRepository.getReferenceById(contaResponse.contaId());

        Imobiliaria imobiliaria = criarImobiliaria(imobiliariaRequestDTO, enderecoRequestDTO, conta);

        Usuario usuario = criarUsuario(userCredResquestDTO.nomeExibicao(), conta, PerfilUsuarioEnum.ROLE_IMOBILIARIA);
        String senhaHash = criptografarSenha(userCredResquestDTO.senha());
        CredencialUsuario credencialUsuario = criarCredencial(
                userCredResquestDTO.login(),
                userCredResquestDTO.email(),
                usuario,
                ProviderTypeEnum.LOGIN_LOCAL,
                senhaHash);
        usuario.adicionarCredencial(credencialUsuario);

        usuarioRepository.save(usuario);
        imobiliariaRepository.save(imobiliaria);

        return authMapper.responseImobiliaria(contaResponse, imobiliaria, usuario);
    }

    private Imobiliaria criarImobiliaria(ImobiliariaCreateRequestDto imobiliariaDto, EnderecoCreateRequestDTO enderecoDto, Conta conta) {
        Endereco endereco = Endereco.builder()
                .logradouro(enderecoDto.logradouro())
                .numero(enderecoDto.numero())
                .complemento(enderecoDto.complemento())
                .bairro(enderecoDto.bairro())
                .cidade(enderecoDto.cidade())
                .uf(enderecoDto.uf())
                .cep(enderecoDto.cep())
                .build();

        return Imobiliaria.builder()
                .conta(conta)
                .endereco(endereco)
                .nomeFantasia(imobiliariaDto.nomeFantasia())
                .bio(imobiliariaDto.bio())
                .fotoUrl(imobiliariaDto.fotoUrl())
                .build();
    }

    private CorretorConta criarCorretorConta(Corretor corretor, Conta conta) {
        return CorretorConta.builder()
                .corretor(corretor)
                .conta(conta)
                .emailProfissional(conta.getEmailResponsavel())
                .build();
    }

    private CredencialUsuario criarCredencial(String login, String email, Usuario usuario, ProviderTypeEnum providerType, String senha) {
        return CredencialUsuario.builder()
                .usuario(usuario)
                .providerType(providerType)
                .providerUserId(login)
                .email(email)
                .passwordHash(senha)
                .build();
    }

    private Corretor criarCorretor(CorretorCreateRequestDTO dto, Usuario usuario) {
        return Corretor.builder()
                .usuario(usuario)
                .nome(dto.nome())
                .cpf(dto.cpf())
                .bio(dto.bio())
                .creci(dto.creci())
                .build();
    }

    private Usuario criarUsuario(String nomeExibicao, Conta conta, PerfilUsuarioEnum perfil) {
        return Usuario.builder()
                .conta(conta)
                .nomeExibicao(nomeExibicao)
                .perfilUsuario(Set.of(perfil))
                .build();
    }

    private String criptografarSenha(String senha) {
        return encoder.encode(senha);
    }

}
