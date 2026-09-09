package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.dto.atendimento.AtendimentoCreateRequestDTO;
import com.nerdev.auxcorretor.dto.atendimento.AtendimentoFindResponseDTO;
import com.nerdev.auxcorretor.dto.atendimento.AtendimentoResponseDTO;
import com.nerdev.auxcorretor.dto.atendimento.AtendimentoUpdateRequestDTO;
import com.nerdev.auxcorretor.dto.cliente.ClienteResumoDTO;
import com.nerdev.auxcorretor.dto.imovel.ImovelResumoDTO;
import com.nerdev.auxcorretor.model.Atendimento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class, CorretorMapper.class})
public interface AtendimentoMapper {

    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "corretor", ignore = true)
    Atendimento toEntity(AtendimentoCreateRequestDTO dto);

    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "corretor", ignore = true)
    @Mapping(target = "statusAtendimento",source = "status")
    @Mapping(target = "visitas", ignore = true)
    void updateEntity(@MappingTarget Atendimento entity, AtendimentoUpdateRequestDTO dto);

    @Mapping(source = "cliente.id", target = "idCliente")
    @Mapping(source = "corretor.id", target = "idCorretor")
    @Mapping(source = "statusAtendimento",target = "status")
    AtendimentoResponseDTO toDTO(Atendimento atendimento);

    AtendimentoFindResponseDTO toFindDTO(Atendimento atendimento);

    ImovelResumoDTO toResumoDTO(Atendimento atendimento);

}
