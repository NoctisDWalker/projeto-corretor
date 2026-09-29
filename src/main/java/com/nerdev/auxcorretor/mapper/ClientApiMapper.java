package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.dto.ClientApi.ClientApiRequestDTO;
import com.nerdev.auxcorretor.dto.ClientApi.ClientApiResponseDTO;
import com.nerdev.auxcorretor.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientApiMapper {

    ClientApiResponseDTO toDto(Client client);
    Client toEntity(ClientApiRequestDTO dto);

}
