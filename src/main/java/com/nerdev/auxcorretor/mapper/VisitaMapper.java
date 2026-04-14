package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.dto.VisitaCreateRequestDTO;
import com.nerdev.auxcorretor.dto.VisitaResponseDTO;
import com.nerdev.auxcorretor.dto.VisitaUpdateRequestDTO;
import com.nerdev.auxcorretor.model.Visita;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")
public interface VisitaMapper {

    @Mapping(target = "atendimento", ignore = true)
    @Mapping(target = "imovel", ignore = true)
    @Mapping(target = "statusVisita", ignore = true)
    @Mapping(target = "dataHoraRealizada", ignore = true)
    @Mapping(target = "interesseCliente", ignore = true)
    Visita toEntity(VisitaCreateRequestDTO dto);

    @Mapping(target = "atendimento", ignore = true)
    @Mapping(target = "imovel", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntity(@MappingTarget Visita entity, VisitaUpdateRequestDTO dto);

    @Mapping(source = "atendimento.id", target = "idAtendimento")
    @Mapping(source = "imovel.id", target = "idImovel")
    VisitaResponseDTO toDTO(Visita entity);
}
