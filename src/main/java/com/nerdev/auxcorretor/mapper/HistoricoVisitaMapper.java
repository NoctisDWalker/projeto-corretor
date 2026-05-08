package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.dto.HistoricoVisitaResponseDTO;
import com.nerdev.auxcorretor.model.historicos.HistoricoVisita;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface HistoricoVisitaMapper {

    HistoricoVisitaResponseDTO toDto(HistoricoVisita entity);
    List<HistoricoVisitaResponseDTO> toDtoList(List<HistoricoVisita> entities);

}
