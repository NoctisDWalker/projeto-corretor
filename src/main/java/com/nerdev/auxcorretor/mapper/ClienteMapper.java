package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.dto.ClienteResponseDTO;
import com.nerdev.auxcorretor.dto.ClienteResquestDTO;
import com.nerdev.auxcorretor.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public abstract class ClienteMapper {

    @Mapping(target = "corretor", source = "corretor.id")
    public abstract Cliente toEntity(ClienteResquestDTO dto);

    public abstract ClienteResponseDTO toDto(Cliente entity);

}
