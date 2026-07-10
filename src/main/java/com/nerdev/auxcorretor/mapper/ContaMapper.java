package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.dto.conta.ContaCreateRequestDTO;
import com.nerdev.auxcorretor.dto.conta.ContaResponseDTO;
import com.nerdev.auxcorretor.dto.conta.ContaUpdateRequestDTO;
import com.nerdev.auxcorretor.model.Conta;
import org.mapstruct.*;

@Mapper(componentModel = "Spring")
public interface ContaMapper {

    @Mapping(target = "usuarios", ignore = true)
    @Mapping(target = "corretorContas", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "dataCancelamento", ignore = true)
    Conta toEntity(ContaCreateRequestDTO createDto);

    @Mapping(target = "usuarios", ignore = true)
    @Mapping(target = "corretorContas", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ContaUpdateRequestDTO updateDto, @MappingTarget Conta entity);

    ContaResponseDTO toDto(Conta conta);

}
