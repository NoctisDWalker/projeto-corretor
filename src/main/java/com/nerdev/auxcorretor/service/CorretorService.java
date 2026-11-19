package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.corretor.CorretorFindResponseDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorResponseDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorUpdateRequestDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.exception.NotFoundException;
import com.nerdev.auxcorretor.mapper.CorretorMapper;
import com.nerdev.auxcorretor.model.Conta;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.model.Usuario;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import com.nerdev.auxcorretor.repository.specs.CorretorSpecs;
import com.nerdev.auxcorretor.repository.specs.SpecificationBuilder;
import com.nerdev.auxcorretor.validation.CorretorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static com.nerdev.auxcorretor.repository.specs.CorretorSpecs.*;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CorretorService {

    private final CorretorRepository corretorRepository;
    private final CorretorMapper corretorMapper;
    private final CorretorValidator corretorValidator;
    private final ServiceUtils serviceUtils;


    public CorretorResponseDTO atualizar(UUID id, CorretorUpdateRequestDTO dto) {
        Corretor corretorEncontrado = buscarCorretorNoEscopo(id);
        corretorValidator.validarAtualizar(corretorEncontrado);
        corretorMapper.updateEntity(corretorEncontrado, dto);

        Corretor corretorSalvo = corretorRepository.save(corretorEncontrado);

        return corretorMapper.toResponseDTO(corretorSalvo);
    }

    public void deletar(UUID id) {
        Corretor corretorEncontrado = buscarCorretorNoEscopo(id);
        corretorValidator.validarDeletar(corretorEncontrado);
        corretorEncontrado.setAtivo(false);
        corretorRepository.save(corretorEncontrado);
    }

    public void reativarCorretor(UUID id) {
        Corretor corretorEncontrado = buscarCorretorNoEscopo(id);
        if (corretorEncontrado.isAtivo()) {
            throw new BusinessException("Somente corretores inativos podem ser reativados");
        }
        corretorEncontrado.setAtivo(true);
        corretorRepository.save(corretorEncontrado);
    }

    public CorretorResponseDTO buscarCorretorPorId(UUID id) {
        Corretor corretorEncontrado = buscarCorretorNoEscopo(id);
        return corretorMapper.toResponseDTO(corretorEncontrado);
    }

    public List<CorretorResponseDTO> listarCorretores() {
        Conta contaLogado = getContaLogado();
        List<Corretor> corretores = corretorRepository.findAllByUsuarioContaIdAndUsuarioDeletedAtIsNull(contaLogado.getId());

        return corretorMapper.toDtoList(corretores);
    }

    public Page<CorretorFindResponseDTO> pesquisaCorretores(
            UUID id,
            String nome,
            String cpf,
            String creci,
            Integer pagina,
            Integer tamanho
    ) {

        Conta contaLogado = getContaLogado();

        Specification<Corretor> spec = new SpecificationBuilder<Corretor>()
                .and(id, CorretorSpecs::idEqual)
                .and(contaLogado.getId(), CorretorSpecs::contaIdEqual)
                .andIfNotBlank(nome, CorretorSpecs::nomeLike)
                .andIfNotBlank(cpf, CorretorSpecs::cpfLike)
                .andIfNotBlank(creci, CorretorSpecs::creciLike)
                .build();

        Pageable pageable = PageRequest.of(pagina, tamanho);
        Page<Corretor> page = corretorRepository.findAll(spec, pageable);
        return page.map(corretorMapper::toFindDto);
    }

    private Conta getContaLogado() {
        return serviceUtils.obterContaLogado();
    }

    private Corretor buscarCorretorNoEscopo(UUID id) {
        Conta contaLogado = getContaLogado();
        corretorValidator.validaPertencimentoDoCorretorAlvo(id, contaLogado.getId());
        return corretorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Corretor não encontrado."));
    }


}