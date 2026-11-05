package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.dto.cliente.*;
import com.nerdev.auxcorretor.model.Cliente;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "corretor", ignore = true)
    Cliente toEntity(ClienteCreateRequestDTO dto);

    @Mapping(target = "corretor", ignore = true)
    void updateEntity(@MappingTarget Cliente entity, ClienteUpdateRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClienteResponseDTO toDto(Cliente entity);

    @Mapping(source = "statusCliente", target = "status")
    ClienteFindResponseDto toFindDto(Cliente entity);

    ClienteResumoDTO toResumoDto(Cliente entity);

}
