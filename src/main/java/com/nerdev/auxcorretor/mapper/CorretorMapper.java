package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.dto.corretor.CorretorResponseDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorUpdateRequestDTO;
import com.nerdev.auxcorretor.model.Corretor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface CorretorMapper {
    void updateEntity(@MappingTarget Corretor Entity, CorretorUpdateRequestDTO dto);
    CorretorResponseDTO toResponseDTO(Corretor entity);
    List<CorretorResponseDTO> toDtoList(List<Corretor> entities);
}
