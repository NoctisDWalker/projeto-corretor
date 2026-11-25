package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.dto.imobiliaria.ImobiliariaResponseDTO;
import com.nerdev.auxcorretor.dto.imobiliaria.endereco.EnderecoResponseDTO;
import com.nerdev.auxcorretor.model.Imobiliaria;
import org.springframework.stereotype.Component;

@Component
public class ImobiliariaMapper {

    public ImobiliariaResponseDTO toResponse (Imobiliaria imobiliaria) {

        if (imobiliaria == null) return null;

        EnderecoResponseDTO enderecoDto = new EnderecoResponseDTO(
              imobiliaria.getEndereco().getLogradouro(),
                imobiliaria.getEndereco().getNumero(),
                imobiliaria.getEndereco().getComplemento(),
                imobiliaria.getEndereco().getBairro(),
                imobiliaria.getEndereco().getCidade(),
                imobiliaria.getEndereco().getUf(),
                imobiliaria.getEndereco().getCep()
        );

        return new ImobiliariaResponseDTO(
                imobiliaria.getId(),
                imobiliaria.getNomeFantasia(),
                imobiliaria.getBio(),
                imobiliaria.getFotoUrl(),
                enderecoDto
        );

    }

}
