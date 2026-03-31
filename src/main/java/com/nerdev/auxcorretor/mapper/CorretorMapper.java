package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.dto.CorretorCreateRequestDTO;
import com.nerdev.auxcorretor.dto.CorretorResponseDTO;
import com.nerdev.auxcorretor.dto.CorretorUpdateRequestDTO;
import com.nerdev.auxcorretor.model.Corretor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface CorretorMapper {
    Corretor toEntity(CorretorCreateRequestDTO dto);
    void updateEntity(@MappingTarget Corretor Entity, CorretorUpdateRequestDTO dto);
    CorretorResponseDTO toResponseDTO(Corretor entity);
}
