package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.atendimento.AtendimentoCreateRequestDTO;
import com.nerdev.auxcorretor.dto.atendimento.AtendimentoFindResponseDTO;
import com.nerdev.auxcorretor.dto.atendimento.AtendimentoResponseDTO;
import com.nerdev.auxcorretor.dto.atendimento.AtendimentoUpdateRequestDTO;
import com.nerdev.auxcorretor.exception.NotFoundException;
import com.nerdev.auxcorretor.mapper.AtendimentoMapper;
import com.nerdev.auxcorretor.model.Atendimento;
import com.nerdev.auxcorretor.model.Cliente;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.model.enums.StatusAtendimentoEnum;
import com.nerdev.auxcorretor.repository.AtendimentoRepository;
import com.nerdev.auxcorretor.repository.ClienteRepository;
import com.nerdev.auxcorretor.repository.specs.AtendimentoSpecs;
import com.nerdev.auxcorretor.repository.specs.SpecificationBuilder;
import com.nerdev.auxcorretor.validation.AtendimentoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final ClienteRepository clienteRepository;
    private final AtendimentoMapper atendimentoMapper;
    private final AtendimentoValidator atendimentoValidator;
    private final ServiceUtils serviceUtils;

    public AtendimentoResponseDTO salvar(AtendimentoCreateRequestDTO createDto) {
        Atendimento atendimento = atendimentoMapper.toEntity(createDto);

        Corretor corretorLogado = getCorretorLogado();
        final Cliente cliente = clienteRepository.findByIdAndCorretorId(createDto.idCliente(), corretorLogado.getId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));


        prepararNovoAtendimento(atendimento, corretorLogado, cliente);
        atendimentoValidator.validaSalvar(atendimento);
        Atendimento salvo = atendimentoRepository.save(atendimento);

        return atendimentoMapper.toDTO(salvo);
    }

    public AtendimentoResponseDTO atualizar(UUID id, AtendimentoUpdateRequestDTO updateDTO) {

        Corretor corretorLogado = getCorretorLogado();
        Atendimento atendimentoEncontrado = atendimentoRepository.findByIdAndCorretorId(id, corretorLogado.getId())
                .orElseThrow(() -> new NotFoundException("Atendimento não encontrado"));

        atendimentoValidator.validaAtualizar(atendimentoEncontrado);
        StatusAtendimentoEnum statusAnterior = atendimentoEncontrado.getStatusAtendimento();
        atendimentoMapper.updateEntity(atendimentoEncontrado, updateDTO);


        regraDataFim(atendimentoEncontrado, statusAnterior);
        Atendimento atualizado = atendimentoRepository.save(atendimentoEncontrado);

        return atendimentoMapper.toDTO(atualizado);
    }

    public void deletar(UUID id) {
        Corretor corretorLogado = getCorretorLogado();
        Atendimento atendimentoEncontrado = atendimentoRepository.findByIdAndCorretorId(id, corretorLogado.getId())
                .orElseThrow(() -> new NotFoundException("Atendimento não encontrado."));

        atendimentoValidator.validaDeletar(atendimentoEncontrado);
        atendimentoEncontrado.setStatusAtendimento(StatusAtendimentoEnum.INATIVO);
        atendimentoEncontrado.setDataFim(LocalDateTime.now());
        atendimentoRepository.save(atendimentoEncontrado);
    }

    public AtendimentoResponseDTO buscarPorId(UUID id) {
        Corretor corretorLogado = getCorretorLogado();
        Atendimento atendimentoEncontrado = atendimentoRepository.findByIdAndCorretorId(id, corretorLogado.getId())
                .orElseThrow(() -> new NotFoundException("Atendimento não encontrado"));

        return atendimentoMapper.toDTO(atendimentoEncontrado);
    }

    public Page<AtendimentoFindResponseDTO> pesquisaAtendimento(
            UUID idCliente,
            StatusAtendimentoEnum statusAtendimento,
            LocalDate menorDataCadastro,
            LocalDate maiorDataCadastro,
            LocalDate menorDataFim,
            LocalDate maiorDataFim,
            Integer pagina,
            Integer tamanho
    ) {

        Corretor corretorLogado = getCorretorLogado();

        Specification<Atendimento> spec = new SpecificationBuilder<Atendimento>()
                .and(idCliente, AtendimentoSpecs::idClienteEquals)
                .and(corretorLogado.getId(), AtendimentoSpecs::idCorretorEquals)
                .and(statusAtendimento, AtendimentoSpecs::statusAtendimentoEquals)
                .and(menorDataCadastro, AtendimentoSpecs::menorDataCadastro)
                .and(maiorDataCadastro, AtendimentoSpecs::maiorDataCadastro)
                .and(menorDataFim, AtendimentoSpecs::menorDataFim)
                .and(maiorDataFim, AtendimentoSpecs::maiorDataFim)
                .build();

        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by("dataCadastro").descending());
        Page<Atendimento> page = atendimentoRepository.findAll(spec, pageable);

        return page.map(atendimentoMapper::toFindDTO);
    }

    private void prepararNovoAtendimento(Atendimento atendimento, Corretor corretorLogado, Cliente cliente) {
        atendimento.setCorretor(corretorLogado);
        atendimento.setCliente(cliente);
        atendimento.setStatusAtendimento(StatusAtendimentoEnum.NOVO_LEAD);
    }

    private Corretor getCorretorLogado() {
        return serviceUtils.obterCorretorLogado();
    }

    private void regraDataFim(Atendimento atendimento, StatusAtendimentoEnum statusAnterior) {
        StatusAtendimentoEnum novoStatus = atendimento.getStatusAtendimento();
        boolean eraFinal = StatusAtendimentoEnum.statusFinais().contains(statusAnterior);
        boolean virouFinal = StatusAtendimentoEnum.statusFinais().contains(novoStatus);

        if (!eraFinal && virouFinal) {
            atendimento.setDataFim(LocalDateTime.now());
        }
        if (eraFinal && !virouFinal) {
            atendimento.setDataFim(null);
        }
    }

}
