package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.ImovelCreateRequestDTO;
import com.nerdev.auxcorretor.dto.ImovelResponseDTO;
import com.nerdev.auxcorretor.dto.ImovelUpdateRequestDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.mapper.ImovelMapper;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.model.Imovel;
import com.nerdev.auxcorretor.model.enums.StatusImovelEnum;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import com.nerdev.auxcorretor.repository.ImovelRepository;
import com.nerdev.auxcorretor.validation.ImovelValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImovelService {

    private final ImovelRepository imovelRepository;
    private final ImovelMapper imovelMapper;
    private final ImovelValidator imovelValidator;
    private final CorretorRepository corretorRepository;

    public ImovelResponseDTO salvar(ImovelCreateRequestDTO createRequestDTO) {
        Corretor corretorLogado = buscaCorretorLogado();

        Imovel imovelEntity = imovelMapper.toEntity(createRequestDTO);
        prepararNovoImovel(imovelEntity, corretorLogado);
        imovelValidator.validaCadastroImovel(imovelEntity);
        Imovel salvo = imovelRepository.save(imovelEntity);

        return imovelMapper.toDTO(salvo);
    }

    public ImovelResponseDTO atualizar(UUID idImovel, ImovelUpdateRequestDTO updateRequestDTO) {
        Corretor corretorLogado = buscaCorretorLogado();

        Imovel imovelPersistido = imovelRepository.findById(idImovel)
                .orElseThrow(() -> new BusinessException("Imovel não encontrado"));

        Imovel imovelAtualizado = imovelMapper.cloneEntity(updateRequestDTO);
        imovelValidator.validaAtualizar(imovelPersistido, imovelAtualizado, corretorLogado);

        imovelMapper.updateEntity(imovelPersistido, updateRequestDTO);
        Imovel atualizado = imovelRepository.save(imovelPersistido);

        return imovelMapper.toDTO(atualizado);
    }

    public void inativar(UUID idImovel) {
        Corretor corretorLogado = buscaCorretorLogado();
        Imovel imovelPersistido = imovelRepository.findById(idImovel)
                .orElseThrow(() -> new BusinessException("Imovel não encontrado"));

        imovelValidator.validaInativar(imovelPersistido, corretorLogado);
        imovelPersistido.setStatusImovel(StatusImovelEnum.INATIVO);
        imovelRepository.save(imovelPersistido);
    }

    public void reativar(UUID idImovel) {
        Corretor corretorLogado = buscaCorretorLogado();
        Imovel imovelPersistido = imovelRepository.findById(idImovel)
                .orElseThrow(() -> new BusinessException("Imovel não encontrado"));

        imovelValidator.validaReativar(imovelPersistido, corretorLogado);
        imovelPersistido.setStatusImovel(StatusImovelEnum.DISPONIVEL);
        imovelRepository.save(imovelPersistido);
    }

    public ImovelResponseDTO buscarPorId(UUID idImovel) {
        Imovel imovelEncontrado = imovelRepository.findById(idImovel)
                .orElseThrow(() -> new BusinessException("Imovel não encontrado"));
        return imovelMapper.toDTO(imovelEncontrado);
    }

    public List<ImovelResponseDTO> buscarMeusImoveis() {
        Corretor corretorLogado = buscaCorretorLogado();
        List<Imovel> imoveis = imovelRepository.findByCorretorResponsavelId(corretorLogado.getId());

        return imoveis.stream().map(imovelMapper::toDTO).toList();
    }

    public List<ImovelResponseDTO> listarTodosImoveis() {
        List<Imovel> imoveis = imovelRepository.findAll();
        return imoveis.stream().map(imovelMapper::toDTO).toList();
    }

    private void prepararNovoImovel(Imovel imovelEntity, Corretor corretorLogado) {
        imovelEntity.setStatusImovel(StatusImovelEnum.DISPONIVEL);
        imovelEntity.setCorretorResponsavel(corretorLogado);
        imovelEntity.setQuantidadeVisitas(0);
    }

    private Corretor buscaCorretorLogado() {
        String idCorretor = "e447e911-a647-4bbd-ac0e-eed4b4660cdf";
        Corretor corretorLogado = corretorRepository.findById(UUID.fromString(idCorretor))
                .orElseThrow(() -> new BusinessException("Corretor não encontrado"));
        return corretorLogado;
    }


}
