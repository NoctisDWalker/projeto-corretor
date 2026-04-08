package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.dto.ClienteCreateRequestDTO;
import com.nerdev.auxcorretor.dto.ClienteResponseDTO;
import com.nerdev.auxcorretor.dto.ClienteUpdateRequestDTO;
import com.nerdev.auxcorretor.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")
public interface ClienteMapper {

    public abstract Cliente toEntity(ClienteCreateRequestDTO dto);
    void updateEntity(@MappingTarget Cliente Entity, ClienteUpdateRequestDTO dto);
    public abstract ClienteResponseDTO toDto(Cliente entity);

}
