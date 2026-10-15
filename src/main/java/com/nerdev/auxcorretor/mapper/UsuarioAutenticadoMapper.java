package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.model.Usuario;
import com.nerdev.auxcorretor.security.UsuarioAutenticado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UsuarioAutenticadoMapper {

    @Mapping(source = "conta.id", target = "contaId")
    UsuarioAutenticado toUsuarioAutenticado(Usuario usuario);

}
