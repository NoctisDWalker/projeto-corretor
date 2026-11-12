package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.imovel.ImovelCreateRequestDTO;
import com.nerdev.auxcorretor.dto.imovel.ImovelFindResponseDTO;
import com.nerdev.auxcorretor.dto.imovel.ImovelResponseDTO;
import com.nerdev.auxcorretor.dto.imovel.ImovelUpdateRequestDTO;
import com.nerdev.auxcorretor.exception.NotFoundException;
import com.nerdev.auxcorretor.mapper.ImovelMapper;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.model.Imovel;
import com.nerdev.auxcorretor.model.enums.CompartilhamentoImovelEnum;
import com.nerdev.auxcorretor.model.enums.FinalidadeImovelEnum;
import com.nerdev.auxcorretor.model.enums.StatusImovelEnum;
import com.nerdev.auxcorretor.model.enums.TipoImovelEnum;
import com.nerdev.auxcorretor.repository.ImovelRepository;
import com.nerdev.auxcorretor.repository.specs.ImovelSpecs;
import com.nerdev.auxcorretor.repository.specs.SpecificationBuilder;
import com.nerdev.auxcorretor.validation.ImovelValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
    private final ServiceUtils serviceUtils;

    public ImovelResponseDTO salvar(ImovelCreateRequestDTO createRequestDTO) {
        Corretor corretorLogado = getCorretor();

        Imovel imovelEntity = imovelMapper.toEntity(createRequestDTO);
        prepararNovoImovel(imovelEntity, corretorLogado);
        imovelValidator.validaCadastroImovel(imovelEntity);
        Imovel salvo = imovelRepository.save(imovelEntity);

        return imovelMapper.toDTO(salvo);
    }

    public ImovelResponseDTO atualizar(UUID idImovel, ImovelUpdateRequestDTO updateRequestDTO) {
        Corretor corretorLogado = getCorretor();

        Imovel imovelPersistido = imovelRepository.findByIdAndCorretorResponsavelId(idImovel, corretorLogado.getId())
                .orElseThrow(() -> new NotFoundException("Imovel não encontrado"));

        Imovel imovelAtualizado = imovelMapper.cloneEntity(updateRequestDTO);
        imovelValidator.validaAtualizar(imovelPersistido, imovelAtualizado);

        imovelMapper.updateEntity(imovelPersistido, updateRequestDTO);
        Imovel atualizado = imovelRepository.save(imovelPersistido);

        return imovelMapper.toDTO(atualizado);
    }

    public void inativar(UUID idImovel) {
        Corretor corretorLogado = getCorretor();
        Imovel imovelPersistido = imovelRepository.findByIdAndCorretorResponsavelId(idImovel, corretorLogado.getId())
                .orElseThrow(() -> new NotFoundException("Imovel não encontrado"));

        imovelValidator.validaInativar(imovelPersistido);
        imovelPersistido.setStatusImovel(StatusImovelEnum.INATIVO);
        imovelRepository.save(imovelPersistido);
    }

    public void reativar(UUID idImovel) {
        Corretor corretorLogado = getCorretor();
        Imovel imovelPersistido = imovelRepository.findByIdAndCorretorResponsavelId(idImovel, corretorLogado.getId())
                .orElseThrow(() -> new NotFoundException("Imovel não encontrado"));

        imovelValidator.validaReativar(imovelPersistido);
        imovelPersistido.setStatusImovel(StatusImovelEnum.DISPONIVEL);
        imovelRepository.save(imovelPersistido);
    }

    public ImovelResponseDTO buscarPorId(UUID idImovel) {
        Corretor corretorLogado = getCorretor();
        Imovel imovelEncontrado = imovelRepository.findById(idImovel)
                .orElseThrow(() -> new NotFoundException("Imovel não encontrado"));
        imovelValidator.validaVisibilidade(imovelEncontrado, corretorLogado.getId());
        return imovelMapper.toDTO(imovelEncontrado);
    }

    public Page<ImovelFindResponseDTO> listarTodosImoveis(
            String titulo,
            BigDecimal valorMinimo,
            BigDecimal valorMaximo,
            FinalidadeImovelEnum finalidadeImovel,
            TipoImovelEnum tipoImovel,
            String cidade,
            String bairro,
            UUID corretorResponsavelId,
            LocalDate dataCadastroInicial,
            LocalDate dataCadastroFinal,
            Integer pagina,
            Integer tamanho
    ) {

        Specification<Imovel> spec = new SpecificationBuilder<Imovel>()
                .andIfNotBlank(titulo, ImovelSpecs::tituloLike)
                .and(valorMinimo, ImovelSpecs::valorMinimo)
                .and(valorMaximo, ImovelSpecs::valorMaximo)
                .and(finalidadeImovel, ImovelSpecs::finalidadeEquals)
                .and(CompartilhamentoImovelEnum.COMPARTILHADO, ImovelSpecs::compartilhamentoImovel)
                .and(tipoImovel, ImovelSpecs::tipoEquals)
                .andIfNotBlank(cidade, ImovelSpecs::cidadeLike)
                .andIfNotBlank(bairro, ImovelSpecs::bairroLike)
                .and(true, f -> ImovelSpecs.statusNotFinalizado())
                .and(corretorResponsavelId, ImovelSpecs::corretorResponsavelEquals)
                .and(dataCadastroInicial, ImovelSpecs::dataCadastroInicial)
                .and(dataCadastroFinal, ImovelSpecs::dataCadastroFinal)
                .build();

        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by("dataCadastro").descending());
        Page<Imovel> page = imovelRepository.findAll(spec, pageable);

        return page.map(imovelMapper::toFindDTO);
    }

    public Page<ImovelFindResponseDTO> pesquisaMeusImoveis(
            String titulo,
            BigDecimal valorMinimo,
            BigDecimal valorMaximo,
            FinalidadeImovelEnum finalidadeImovel,
            CompartilhamentoImovelEnum compartilhamentoImovel,
            TipoImovelEnum tipoImovel,
            String cidade,
            String bairro,
            StatusImovelEnum statusImovel,
            LocalDate dataCadastroInicial,
            LocalDate dataCadastroFinal,
            Integer pagina,
            Integer tamanho
    ) {

        Corretor corretorLogado = getCorretor();

        Specification<Imovel> spec = new SpecificationBuilder<Imovel>()
                .andIfNotBlank(titulo, ImovelSpecs::tituloLike)
                .and(valorMinimo, ImovelSpecs::valorMinimo)
                .and(valorMaximo, ImovelSpecs::valorMaximo)
                .and(finalidadeImovel, ImovelSpecs::finalidadeEquals)
                .and(compartilhamentoImovel, ImovelSpecs::compartilhamentoImovel)
                .and(tipoImovel, ImovelSpecs::tipoEquals)
                .andIfNotBlank(cidade, ImovelSpecs::cidadeLike)
                .andIfNotBlank(bairro, ImovelSpecs::bairroLike)
                .and(statusImovel, ImovelSpecs::statusEquals)
                .and(corretorLogado.getId(), ImovelSpecs::corretorResponsavelEquals)
                .and(dataCadastroInicial, ImovelSpecs::dataCadastroInicial)
                .and(dataCadastroFinal, ImovelSpecs::dataCadastroFinal)
                .build();

        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by("dataCadastro").descending());
        Page<Imovel> page = imovelRepository.findAll(spec, pageable);

        return page.map(imovelMapper::toFindDTO);
    }

    private void prepararNovoImovel(Imovel imovelEntity, Corretor corretorLogado) {
        imovelEntity.setStatusImovel(StatusImovelEnum.DISPONIVEL);
        imovelEntity.setCorretorResponsavel(corretorLogado);
        imovelEntity.setQuantidadeVisitas(0);

        if (imovelEntity.getCompartilhamentoImovel() == null) {
            imovelEntity.setCompartilhamentoImovel(CompartilhamentoImovelEnum.PRIVADO);
        }
    }

    private Corretor getCorretor() {
        return serviceUtils.obterCorretorLogado();
    }

}

