package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.imovel.ImovelCreateRequestDTO;
import com.nerdev.auxcorretor.dto.imovel.ImovelFindResponseDTO;
import com.nerdev.auxcorretor.dto.imovel.ImovelResponseDTO;
import com.nerdev.auxcorretor.dto.imovel.ImovelUpdateRequestDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.mapper.ImovelMapper;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.model.Imovel;
import com.nerdev.auxcorretor.model.enums.FinalidadeImovelEnum;
import com.nerdev.auxcorretor.model.enums.StatusImovelEnum;
import com.nerdev.auxcorretor.model.enums.TipoImovelEnum;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import com.nerdev.auxcorretor.repository.ImovelRepository;
import com.nerdev.auxcorretor.repository.specs.ImovelSpecs;
import com.nerdev.auxcorretor.validation.ImovelValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import static com.nerdev.auxcorretor.repository.specs.ImovelSpecs.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
        String idCorretor = "b1452570-2e78-4487-8c40-b90de9b79dac";
        Corretor corretorLogado = corretorRepository.findById(UUID.fromString(idCorretor))
                .orElseThrow(() -> new BusinessException("Corretor não encontrado"));
        return corretorLogado;
    }

    public Page<ImovelFindResponseDTO> pesquisaImovel(
            String titulo,
            BigDecimal valorMinimo,
            BigDecimal valorMaximo,
            FinalidadeImovelEnum finalidadeImovel,
            TipoImovelEnum tipoImovel,
            String cidade,
            String bairro,
            StatusImovelEnum statusImovel,
            UUID corretorResponsavelId,
            LocalDate dataCadastro,
            Integer pagina,
            Integer tamanho
    ){
        Specification<Imovel> spec = (root, query, cb) -> cb.conjunction();

        if (isNotNullOrEmpty(titulo)) {
            spec = spec.and(tituloLike(titulo));
        }
        if (valorMinimo != null) {
            spec = spec.and(valorMinimo(valorMinimo));
        }
        if (valorMaximo != null) {
            spec = spec.and(valorMaximo(valorMaximo));
        }
        if (finalidadeImovel != null) {
            spec = spec.and(finalidadeEquals(finalidadeImovel));
        }
        if (tipoImovel != null) {
            spec = spec.and(tipoEquals(tipoImovel));
        }
        if (isNotNullOrEmpty(cidade)) {
            spec = spec.and(cidadeLike(cidade));
        }
        if (isNotNullOrEmpty(bairro)) {
            spec = spec.and(bairroLike(bairro));
        }
        if (statusImovel != null) {
            spec = spec.and(statusEquals(statusImovel));
        }
        if (corretorResponsavelId != null) {
            spec = spec.and(corretorResponsavelEquals(corretorResponsavelId));
        }
        if (dataCadastro != null) {
            spec = spec.and(dataCadastroAte(dataCadastro));
        }

        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by("dataCadastro").descending());
        Page<Imovel> page = imovelRepository.findAll(spec, pageable);

        return page.map(imovelMapper::toFindDTO);
    }

    private boolean isNotNullOrEmpty(String string) {
        return  string != null && !string.isEmpty();
    }

}
