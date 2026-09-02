package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.dto.cliente.ClienteCreateRequestDTO;
import com.nerdev.auxcorretor.dto.cliente.ClienteFindResponseDto;
import com.nerdev.auxcorretor.dto.cliente.ClienteResponseDTO;
import com.nerdev.auxcorretor.dto.cliente.ClienteUpdateRequestDTO;
import com.nerdev.auxcorretor.model.Cliente;
import org.mapstruct.*;

@Mapper(componentModel = "Spring")
public interface ClienteMapper {

    Cliente toEntity(ClienteCreateRequestDTO dto);

    void updateEntity(@MappingTarget Cliente Entity, ClienteUpdateRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClienteResponseDTO toDto(Cliente entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClienteFindResponseDto toFindDto(Cliente entity);

}
