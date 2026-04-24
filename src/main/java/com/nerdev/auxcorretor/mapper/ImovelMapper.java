package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.dto.ImovelCreateRequestDTO;
import com.nerdev.auxcorretor.dto.ImovelResponseDTO;
import com.nerdev.auxcorretor.dto.ImovelUpdateRequestDTO;
import com.nerdev.auxcorretor.model.Imovel;
import org.mapstruct.*;

@Mapper(componentModel = "Spring")
public interface ImovelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statusImovel", ignore = true)
    @Mapping(target = "corretorResponsavel", ignore = true)
    @Mapping(target = "quantidadeVisitas", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    Imovel toEntity(ImovelCreateRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Imovel entity, ImovelUpdateRequestDTO dto);

    @Mapping(source = "corretorResponsavel.id", target = "idCorretorResponsavel")
    ImovelResponseDTO toDTO(Imovel entity);

}
