package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.dto.AtendimentoCreateRequestDTO;
import com.nerdev.auxcorretor.dto.AtendimentoResponseDTO;
import com.nerdev.auxcorretor.dto.AtendimentoUpdateRequestDTO;
import com.nerdev.auxcorretor.model.Atendimento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")
public interface AtendimentoMapper {


    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "corretor", ignore = true)
    Atendimento toEntity(AtendimentoCreateRequestDTO dto);

    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "corretor", ignore = true)
    void updateEntity(@MappingTarget Atendimento entity, AtendimentoUpdateRequestDTO dto);

    @Mapping(source = "cliente.id", target = "idCliente")
    @Mapping(source = "corretor.id", target = "idCorretor")
    @Mapping(source = "statusAtendimento",target = "status")
    AtendimentoResponseDTO toDTO(Atendimento atendimento);

}
