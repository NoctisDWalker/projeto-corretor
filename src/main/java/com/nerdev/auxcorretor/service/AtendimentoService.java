package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.AtendimentoCreateRequestDTO;
import com.nerdev.auxcorretor.dto.AtendimentoResponseDTO;
import com.nerdev.auxcorretor.dto.AtendimentoUpdateRequestDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.mapper.AtendimentoMapper;
import com.nerdev.auxcorretor.model.Atendimento;
import com.nerdev.auxcorretor.model.Cliente;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.model.enums.StatusAtendimentoEnum;
import com.nerdev.auxcorretor.repository.AtendimentoRepository;
import com.nerdev.auxcorretor.repository.ClienteRepository;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import com.nerdev.auxcorretor.validation.AtendimentoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final CorretorRepository corretorRepository;
    private final ClienteRepository clienteRepository;
    private final AtendimentoMapper atendimentoMapper;
    private final AtendimentoValidator atendimentoValidator;

    public AtendimentoResponseDTO salvar(AtendimentoCreateRequestDTO createDto){
        // Todo: Atualizar metodo apos implementação da autenticação
        Atendimento atendimento = atendimentoMapper.toEntity(createDto);

        final Corretor corretor = corretorRepository.findById(createDto.idCorretor())
                .orElseThrow(() -> new BusinessException("Corretor não encontrado."));
        final Cliente cliente = clienteRepository.findById(createDto.idCliente())
                .orElseThrow(() -> new BusinessException("Cliente não encontrado."));

        atendimento.setCorretor(corretor);
        atendimento.setCliente(cliente);
        atendimento.setStatusAtendimento(StatusAtendimentoEnum.NOVO_LEAD);
        atendimentoValidator.validaSalvar(atendimento);
        Atendimento salvo = atendimentoRepository.save(atendimento);

        return atendimentoMapper.toDTO(salvo);
    }

    public AtendimentoResponseDTO atualizar(UUID id, AtendimentoUpdateRequestDTO updateDTO){
        Atendimento atendimentoEncontrado = atendimentoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Atendimento não encontrado"));

        StatusAtendimentoEnum statusAnterior = atendimentoEncontrado.getStatusAtendimento();
        atendimentoMapper.updateEntity(atendimentoEncontrado, updateDTO);
        atendimentoValidator.validaAtualizar(atendimentoEncontrado);
        regraDataFim(atendimentoEncontrado, statusAnterior);
        Atendimento atualizado = atendimentoRepository.save(atendimentoEncontrado);

        return atendimentoMapper.toDTO(atualizado);
    }

    public void deletar(UUID id){
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Atendimento não encontrado."));

        atendimentoValidator.validaDeletar(atendimento);
        atendimento.setStatusAtendimento(StatusAtendimentoEnum.INATIVO);
        atendimento.setDataFim(LocalDateTime.now());
        atendimentoRepository.save(atendimento);
    }

    public AtendimentoResponseDTO buscarPorId(UUID id){
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Atendimento não encontrado"));

        return atendimentoMapper.toDTO(atendimento);
    }

    private void regraDataFim(Atendimento atendimento, StatusAtendimentoEnum statusAnterior){
        StatusAtendimentoEnum novoStatus = atendimento.getStatusAtendimento();
        boolean eraFinal = StatusAtendimentoEnum.statusFinais().contains(statusAnterior);
        boolean virouFinal = StatusAtendimentoEnum.statusFinais().contains(novoStatus);

        if(!eraFinal && virouFinal){
            atendimento.setDataFim(LocalDateTime.now());
        }
        if(eraFinal && !virouFinal){
            atendimento.setDataFim(null);
        }
    }

}
