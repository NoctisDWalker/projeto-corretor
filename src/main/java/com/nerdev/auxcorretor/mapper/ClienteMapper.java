package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.dto.ClienteResponseDTO;
import com.nerdev.auxcorretor.dto.ClienteResquestDTO;
import com.nerdev.auxcorretor.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")
public interface ClienteMapper {

    @Mapping(target = "corretor", source = "corretor.id")
    public abstract Cliente toEntity(ClienteResquestDTO dto);
    void updateEntity(@MappingTarget Cliente Entity, ClienteResquestDTO dto);
    public abstract ClienteResponseDTO toDto(Cliente entity);

}
