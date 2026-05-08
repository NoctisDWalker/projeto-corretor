package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.HistoricoVisitaResponseDTO;
import com.nerdev.auxcorretor.dto.VisitaCreateRequestDTO;
import com.nerdev.auxcorretor.dto.VisitaResponseDTO;
import com.nerdev.auxcorretor.dto.VisitaUpdateRequestDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.mapper.HistoricoVisitaMapper;
import com.nerdev.auxcorretor.mapper.VisitaMapper;
import com.nerdev.auxcorretor.model.Atendimento;
import com.nerdev.auxcorretor.model.Imovel;
import com.nerdev.auxcorretor.model.Visita;
import com.nerdev.auxcorretor.model.enums.StatusVisitaEnum;
import com.nerdev.auxcorretor.model.historicos.HistoricoVisita;
import com.nerdev.auxcorretor.repository.AtendimentoRepository;
import com.nerdev.auxcorretor.repository.ImovelRepository;
import com.nerdev.auxcorretor.repository.VisitaRepository;
import com.nerdev.auxcorretor.validation.VisitaValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VisitaService {

    private final VisitaMapper visitaMapper;
    private final VisitaValidator visitaValidator;
    private final VisitaRepository visitaRepository;
    private final ImovelRepository imovelRepository;
    private final AtendimentoRepository atendimentoRepository;
    private final HistoricoVisitaService historicoVisitaService;
    private final HistoricoVisitaMapper historicoVisitaMapper;

    public VisitaResponseDTO salvar(UUID idAtendimento, VisitaCreateRequestDTO createDto) {
        Atendimento atendimentoEncontrado = atendimentoRepository.findById(idAtendimento)
                .orElseThrow(() -> new BusinessException("Atendimento não encontrado"));
        Imovel imovelEncontrado = imovelRepository.findById(createDto.idImovel())
                .orElseThrow(() -> new BusinessException("Imovel não encontrado"));

        Visita visitaCriada = visitaMapper.toEntity(createDto);
        prepararNovaVisita(visitaCriada, atendimentoEncontrado, imovelEncontrado);

        visitaValidator.validaCadastro(visitaCriada, atendimentoEncontrado);
        Visita visitaSalva = visitaRepository.save(visitaCriada);
        historicoVisitaService.criarHistoricoAutomatico(visitaSalva, "Visita criada.");

        return visitaMapper.toDTO(visitaSalva);
    }

    public VisitaResponseDTO atualizar(UUID idVisita, VisitaUpdateRequestDTO updateDto, UUID idAtendimento) {
        Atendimento atendimentoEncontrado = atendimentoRepository.findById(idAtendimento)
                .orElseThrow(() -> new BusinessException("Atendimento não encontrado"));
        Visita visitaPersistida = visitaRepository.findById(idVisita)
                .orElseThrow(() -> new BusinessException("Visita não encontrada"));

        StatusVisitaEnum statusAntigo = visitaPersistida.getStatusVisita();

        validaRelacionamentoVisitaAtendimento(atendimentoEncontrado, visitaPersistida);

        Visita visitaAtualizada = visitaMapper.cloneEntity(updateDto);
        visitaAtualizada.setId(visitaPersistida.getId());
        visitaAtualizada.setAtendimento(visitaPersistida.getAtendimento());
        visitaAtualizada.setImovel(visitaPersistida.getImovel());

        visitaValidator.validaAtualizar(visitaPersistida, visitaAtualizada, atendimentoEncontrado);
        visitaMapper.updateEntity(visitaPersistida, updateDto);

        Visita atualizada = visitaRepository.save(visitaPersistida);

        StatusVisitaEnum statusNovo = atualizada.getStatusVisita();

        if (statusAntigo != statusNovo) {
            historicoVisitaService.criarHistoricoAutomatico(atualizada, mensagemHistorico(statusNovo));
        }

        return visitaMapper.toDTO(atualizada);
    }

    public void cancelar(UUID id, UUID idAtendimento) {
        Visita visitaEncontrada = visitaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Visita não encontrada"));
        Atendimento atendimentoEncontrado = atendimentoRepository.findById(idAtendimento)
                .orElseThrow(() -> new BusinessException("Atendimento não encontrado"));

        validaRelacionamentoVisitaAtendimento(atendimentoEncontrado, visitaEncontrada);
        visitaValidator.validaCancelar(visitaEncontrada);
        visitaEncontrada.setStatusVisita(StatusVisitaEnum.CANCELADA);

        Visita visitaCancelada = visitaRepository.save(visitaEncontrada);
        historicoVisitaService.criarHistoricoAutomatico(visitaCancelada, mensagemHistorico(visitaCancelada.getStatusVisita()));
    }

    public void reativarVisita(UUID id, UUID idAtendimento) {
        Visita visitaEncontrada = visitaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Visita não encontrada"));
        Atendimento atendimentoEncontrado = atendimentoRepository.findById(idAtendimento)
                .orElseThrow(() -> new BusinessException("Atendimento não encontrado"));

        validaRelacionamentoVisitaAtendimento(atendimentoEncontrado, visitaEncontrada);
        visitaValidator.validaReativar(visitaEncontrada);
        visitaEncontrada.setStatusVisita(StatusVisitaEnum.AGENDADA);

        Visita reativada = visitaRepository.save(visitaEncontrada);
        historicoVisitaService.criarHistoricoAutomatico(reativada, mensagemHistorico(reativada.getStatusVisita()));
    }

    public void adicionarHistoricoManual(UUID idVisita, UUID idAtendimento, String descricao) {
        Visita visitaEncontrada = visitaRepository.findById(idVisita)
                .orElseThrow(() -> new BusinessException("Visita não encontrada"));
        Atendimento atendimentoEncontrado = atendimentoRepository.findById(idAtendimento)
                .orElseThrow(() -> new BusinessException("Atendimento não encontrado"));

        validaRelacionamentoVisitaAtendimento(atendimentoEncontrado, visitaEncontrada);

        historicoVisitaService.criarHistoricoManual(visitaEncontrada, descricao);
    }

    public List<HistoricoVisitaResponseDTO> buscarTimeLineVisita(UUID idVisita, UUID idAtendimento){
        Visita visitaEncontrada = visitaRepository.findById(idVisita)
                .orElseThrow(() -> new BusinessException("Visita não encontrada"));
        Atendimento atendimentoEncontrado = atendimentoRepository.findById(idAtendimento)
                .orElseThrow(() -> new BusinessException("Atendimento não encontrado"));

        validaRelacionamentoVisitaAtendimento(atendimentoEncontrado, visitaEncontrada);

        List<HistoricoVisita> historicos = visitaEncontrada.getHistoricos();

        return historicoVisitaMapper.toDtoList(historicos);
    }

    private void prepararNovaVisita(Visita visitaCriada, Atendimento atendimentoEncontrado, Imovel imovelEncontrado) {
        visitaCriada.setAtendimento(atendimentoEncontrado);
        visitaCriada.setImovel(imovelEncontrado);
        visitaCriada.setStatusVisita(StatusVisitaEnum.AGENDADA);
    }

    private void validaRelacionamentoVisitaAtendimento(Atendimento atendimento, Visita visita) {
        if (!atendimento.getId().equals(visita.getAtendimento().getId())) {
            throw new BusinessException("Visita não pertence ao atendimento informado.");
        }
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
