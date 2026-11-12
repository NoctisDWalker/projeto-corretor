package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.visita.*;
import com.nerdev.auxcorretor.exception.NotFoundException;
import com.nerdev.auxcorretor.mapper.HistoricoVisitaMapper;
import com.nerdev.auxcorretor.mapper.VisitaMapper;
import com.nerdev.auxcorretor.model.Atendimento;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.model.Imovel;
import com.nerdev.auxcorretor.model.Visita;
import com.nerdev.auxcorretor.model.enums.InteresseClienteEnum;
import com.nerdev.auxcorretor.model.enums.StatusVisitaEnum;
import com.nerdev.auxcorretor.model.historicos.HistoricoVisita;
import com.nerdev.auxcorretor.repository.AtendimentoRepository;
import com.nerdev.auxcorretor.repository.ImovelRepository;
import com.nerdev.auxcorretor.repository.VisitaRepository;
import com.nerdev.auxcorretor.repository.specs.SpecificationBuilder;
import com.nerdev.auxcorretor.repository.specs.VisitaSpecs;
import com.nerdev.auxcorretor.validation.ImovelValidator;
import com.nerdev.auxcorretor.validation.VisitaValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VisitaService {

    private final VisitaMapper visitaMapper;
    private final HistoricoVisitaMapper historicoVisitaMapper;
    private final VisitaValidator visitaValidator;
    private final ImovelValidator imovelValidator;
    private final VisitaRepository visitaRepository;
    private final ImovelRepository imovelRepository;
    private final AtendimentoRepository atendimentoRepository;
    private final HistoricoVisitaService historicoVisitaService;
    private final ServiceUtils serviceUtils;

    @Transactional
    public VisitaResponseDTO salvar(UUID idAtendimento, VisitaCreateRequestDTO createDto) {
        Atendimento atendimentoEncontrado = getAtendimento(idAtendimento);
        Imovel imovelEncontrado = getImovel(createDto);

        Visita visitaCriada = visitaMapper.toEntity(createDto);
        prepararNovaVisita(visitaCriada, atendimentoEncontrado, imovelEncontrado);

        visitaValidator.validaCadastro(visitaCriada, atendimentoEncontrado);
        Visita visitaSalva = visitaRepository.save(visitaCriada);
        imovelRepository.imcrementarVisitas(imovelEncontrado.getId());
        historicoVisitaService.criarHistoricoAutomatico(visitaSalva, "Visita criada.");

        return visitaMapper.toDTO(visitaSalva);
    }

    public VisitaResponseDTO atualizar(UUID idVisita, VisitaUpdateRequestDTO updateDto, UUID idAtendimento) {
        Atendimento atendimentoEncontrado = getAtendimento(idAtendimento);
        Visita visitaPersistida = getVisita(idVisita);

        StatusVisitaEnum statusAntigo = visitaPersistida.getStatusVisita();
        validaRelacionamentoVisitaAtendimento(atendimentoEncontrado, visitaPersistida);

        Visita visitaAtualizada = visitaMapper.cloneEntity(updateDto);
        prepararVisitaAtualizada(visitaAtualizada, visitaPersistida);

        visitaValidator.validaAtualizar(visitaPersistida, visitaAtualizada, atendimentoEncontrado);
        visitaMapper.updateEntity(visitaPersistida, updateDto);

        Visita atualizada = visitaRepository.save(visitaPersistida);
        StatusVisitaEnum statusNovo = atualizada.getStatusVisita();

        if (statusAntigo != statusNovo) {
            historicoVisitaService.criarHistoricoAutomatico(atualizada, mensagemHistorico(statusNovo));
        }
        return visitaMapper.toDTO(atualizada);
    }

    public void cancelar(UUID idVisita, UUID idAtendimento) {
        Atendimento atendimentoEncontrado = getAtendimento(idAtendimento);
        Visita visitaEncontrada = getVisita(idVisita);

        validaRelacionamentoVisitaAtendimento(atendimentoEncontrado, visitaEncontrada);
        visitaValidator.validaCancelar(visitaEncontrada);
        visitaEncontrada.setStatusVisita(StatusVisitaEnum.CANCELADA);

        Visita visitaCancelada = visitaRepository.save(visitaEncontrada);
        imovelRepository.decrementarVisitas(visitaEncontrada.getImovel().getId());
        historicoVisitaService.criarHistoricoAutomatico(visitaCancelada, mensagemHistorico(visitaCancelada.getStatusVisita()));
    }

    // Todo: possivelmente excluir este metodo
    public void reativarVisita(UUID idVisita, UUID idAtendimento) {
        Atendimento atendimentoEncontrado = getAtendimento(idAtendimento);
        Visita visitaEncontrada = getVisita(idVisita);

        validaRelacionamentoVisitaAtendimento(atendimentoEncontrado, visitaEncontrada);
        visitaValidator.validaReativar(visitaEncontrada);
        visitaEncontrada.setStatusVisita(StatusVisitaEnum.AGENDADA);

        Visita reativada = visitaRepository.save(visitaEncontrada);
        historicoVisitaService.criarHistoricoAutomatico(reativada, mensagemHistorico(reativada.getStatusVisita()));
    }

    public void adicionarHistoricoManual(UUID idVisita, UUID idAtendimento, String descricao) {
        Visita visitaEncontrada = getVisita(idVisita);
        Atendimento atendimentoEncontrado = getAtendimento(idAtendimento);
        validaRelacionamentoVisitaAtendimento(atendimentoEncontrado, visitaEncontrada);
        historicoVisitaService.criarHistoricoManual(visitaEncontrada, descricao);
    }

    public List<HistoricoVisitaResponseDTO> buscarTimeLineVisita(UUID idVisita, UUID idAtendimento) {
        Visita visitaEncontrada = getVisita(idVisita);
        Atendimento atendimentoEncontrado = getAtendimento(idAtendimento);
        validaRelacionamentoVisitaAtendimento(atendimentoEncontrado, visitaEncontrada);
        List<HistoricoVisita> historicos = visitaEncontrada.getHistoricos();
        return historicoVisitaMapper.toDtoList(historicos);
    }

    public Page<VisitaFindResponseDTO> pesquisaVisita(
            UUID idAtendimento,
            UUID idImovel,
            LocalDate menorDataAgendada,
            LocalDate maiorDataAgendada,
            LocalDate menorDataRealizada,
            LocalDate maiorDataRealizada,
            StatusVisitaEnum statusVisita,
            InteresseClienteEnum interesseCliente,
            LocalDate menorDataCadastro,
            LocalDate maiorDataCadastro,
            Integer pagina,
            Integer tamanho
    ) {

        Corretor corretorEncontrado = getCorretor();

        Specification<Visita> spec = new SpecificationBuilder<Visita>()
                .and(idAtendimento, VisitaSpecs::idAtendimentoEquals)
                .and(corretorEncontrado.getId(), VisitaSpecs::idCorretorResponsavelEquals)
                .and(idImovel, VisitaSpecs::idImovelEquals)
                .and(menorDataAgendada, VisitaSpecs::menorDataAgendada)
                .and(maiorDataAgendada, VisitaSpecs::maiorDataAgendada)
                .and(menorDataRealizada, VisitaSpecs::menorDataRealizada)
                .and(maiorDataRealizada, VisitaSpecs::maiorDataRealizada)
                .and(statusVisita, VisitaSpecs::statusVisitaEqual)
                .and(interesseCliente, VisitaSpecs::interesseClienteEqual)
                .and(menorDataCadastro, VisitaSpecs::menorDataCadastro)
                .and(maiorDataCadastro, VisitaSpecs::maiorDataCadastro)
                .build();

        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by("dataHoraAgendada").descending());
        Page<Visita> page = visitaRepository.findAll(spec, pageable);
        return page.map(visitaMapper::toFindDTO);
    }

    private void prepararNovaVisita(Visita visitaCriada, Atendimento atendimentoEncontrado, Imovel imovelEncontrado) {
        visitaCriada.setAtendimento(atendimentoEncontrado);
        visitaCriada.setImovel(imovelEncontrado);
        visitaCriada.setStatusVisita(StatusVisitaEnum.AGENDADA);
    }

    private static void prepararVisitaAtualizada(Visita visitaAtualizada, Visita visitaPersistida) {
        visitaAtualizada.setId(visitaPersistida.getId());
        visitaAtualizada.setAtendimento(visitaPersistida.getAtendimento());
        visitaAtualizada.setImovel(visitaPersistida.getImovel());
    }

    private void validaRelacionamentoVisitaAtendimento(Atendimento atendimento, Visita visita) {
        if (!atendimento.getId().equals(visita.getAtendimento().getId())) {
            throw new NotFoundException("Visita não pertence ao atendimento informado.");
        }
    }

    private Corretor getCorretor() {
        return serviceUtils.obterCorretorLogado();
    }

    private Atendimento getAtendimento(UUID idAtendimento) {
        Corretor corretorLogado = getCorretor();
        return atendimentoRepository.findByIdAndCorretorId(idAtendimento, corretorLogado.getId())
                .orElseThrow(() -> new NotFoundException("Atendimento não encontrado"));
    }

    private Visita getVisita(UUID idVisita) {
        return visitaRepository.findById(idVisita)
                .orElseThrow(() -> new NotFoundException("Visita não encontrada"));
    }

    private Imovel getImovel(VisitaCreateRequestDTO createDto) {
        Corretor corretorLogado = getCorretor();
        Imovel imovelEncontrado = imovelRepository.findById(createDto.idImovel())
                .orElseThrow(() -> new NotFoundException("Imovel não encontrado"));

        imovelValidator.validaVisibilidade(imovelEncontrado,  corretorLogado.getId());
        return imovelEncontrado;
    }


    private String mensagemHistorico(StatusVisitaEnum status) {

        String mensagem;

        switch (status) {
            case AGENDADA:
                mensagem = "Visita agendada com sucesso";
                break;
            case CONFIRMADA:
                mensagem = "Visita confirmada com sucesso";
                break;
            case REMARCADA:
                mensagem = "Visita remarcada com sucesso";
                break;
            case REALIZADA:
                mensagem = "Visita realizada com sucesso";
                break;
            case CANCELADA:
                mensagem = "Visita cancelada com sucesso";
                break;
            case NAO_COMPARECEU:
                mensagem = "Cliente não compareceu";
                break;
            default:
                mensagem = "Status informado invalido.";
                break;
        }

        return mensagem;
    }

}
