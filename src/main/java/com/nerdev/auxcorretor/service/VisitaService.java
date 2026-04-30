package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.VisitaCreateRequestDTO;
import com.nerdev.auxcorretor.dto.VisitaResponseDTO;
import com.nerdev.auxcorretor.dto.VisitaUpdateRequestDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.mapper.VisitaMapper;
import com.nerdev.auxcorretor.model.Atendimento;
import com.nerdev.auxcorretor.model.Imovel;
import com.nerdev.auxcorretor.model.Visita;
import com.nerdev.auxcorretor.model.enums.StatusVisitaEnum;
import com.nerdev.auxcorretor.repository.AtendimentoRepository;
import com.nerdev.auxcorretor.repository.ImovelRepository;
import com.nerdev.auxcorretor.repository.VisitaRepository;
import com.nerdev.auxcorretor.validation.VisitaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VisitaService {

    private final VisitaMapper visitaMapper;
    private final VisitaValidator visitaValidator;
    private final VisitaRepository visitaRepository;
    private final ImovelRepository imovelRepository;
    private final AtendimentoRepository atendimentoRepository;

    public VisitaResponseDTO salvar(UUID idAtendimento, VisitaCreateRequestDTO createDto){
        Atendimento atendimentoEncontrado = atendimentoRepository.findById(idAtendimento)
                .orElseThrow(() -> new BusinessException("Atendimento não encontrado"));
        Imovel imovelEncontrado = imovelRepository.findById(createDto.idImovel())
                .orElseThrow(() -> new BusinessException("Imovel não encontrado"));

        Visita visitaCriada = visitaMapper.toEntity(createDto);
        prepararNovaVisita(visitaCriada, atendimentoEncontrado, imovelEncontrado);

        visitaValidator.validaCadastro(visitaCriada, atendimentoEncontrado);
        Visita visitaSalva = visitaRepository.save(visitaCriada);

        return visitaMapper.toDTO(visitaSalva);
    }

    public VisitaResponseDTO atualizar(UUID idVisita, VisitaUpdateRequestDTO updateDto, UUID idAtendimento){
        Atendimento atendimentoEncontrado = atendimentoRepository.findById(idAtendimento)
                .orElseThrow(() -> new BusinessException("Atendimento não encontrado"));
        Visita visitaPersistida = visitaRepository.findById(idVisita)
                .orElseThrow(() -> new BusinessException("Visita não encontrada"));

        validaRelacionamentoVisitaAtendimento(atendimentoEncontrado, visitaPersistida);

        Visita visitaAtualizada = visitaMapper.cloneEntity(updateDto);
        visitaAtualizada.setId(visitaPersistida.getId());
        visitaAtualizada.setAtendimento(visitaPersistida.getAtendimento());
        visitaAtualizada.setImovel(visitaPersistida.getImovel());

        visitaValidator.validaAtualizar(visitaPersistida, visitaAtualizada, atendimentoEncontrado);
        visitaMapper.updateEntity(visitaPersistida, updateDto);

        Visita atualizada = visitaRepository.save(visitaPersistida);

        return visitaMapper.toDTO(atualizada);
    }

    public void cancelar(UUID id, UUID idAtendimento){
        Visita visitaEncontrada = visitaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Visita não encontrada"));
        Atendimento atendimentoEncontrado = atendimentoRepository.findById(idAtendimento)
                .orElseThrow(() -> new BusinessException("Atendimento não encontrado"));

        validaRelacionamentoVisitaAtendimento(atendimentoEncontrado, visitaEncontrada);
        visitaValidator.validaCancelar(visitaEncontrada);
        visitaEncontrada.setStatusVisita(StatusVisitaEnum.CANCELADA);

        visitaRepository.save(visitaEncontrada);
    }

    public void reativarVisita(UUID id, UUID idAtendimento){
        Visita visitaEncontrada = visitaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Visita não encontrada"));
        Atendimento atendimentoEncontrado = atendimentoRepository.findById(idAtendimento)
                .orElseThrow(() -> new BusinessException("Atendimento não encontrado"));

        validaRelacionamentoVisitaAtendimento(atendimentoEncontrado, visitaEncontrada);
        visitaValidator.validaReativar(visitaEncontrada);
        visitaEncontrada.setStatusVisita(StatusVisitaEnum.AGENDADA);

        visitaRepository.save(visitaEncontrada);
    }

    private void prepararNovaVisita(Visita visitaCriada, Atendimento atendimentoEncontrado, Imovel imovelEncontrado){
        visitaCriada.setAtendimento(atendimentoEncontrado);
        visitaCriada.setImovel(imovelEncontrado);
        visitaCriada.setStatusVisita(StatusVisitaEnum.AGENDADA);
    }

    private void validaRelacionamentoVisitaAtendimento(Atendimento atendimento, Visita visita){
        if (!atendimento.getId().equals(visita.getAtendimento().getId())){
            throw new BusinessException("Visita não pertence ao atendimento informado.");
        }
    }
}
