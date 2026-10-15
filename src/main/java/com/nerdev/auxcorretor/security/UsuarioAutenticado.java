package com.nerdev.auxcorretor.security;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record UsuarioAutenticado(UUID id,
                                 UUID contaId,
                                 String nomeExibicao,
                                 Set<String> perfilUsuario) implements Serializable {

    public UsuarioAutenticado {
        Objects.requireNonNull(id, "Id obrigatorio");
        Objects.requireNonNull(contaId, "Conta obrigatoria");
        Objects.requireNonNull(nomeExibicao, "Nome obrigatorio");
        Objects.requireNonNull(perfilUsuario, "Perfis obrigatorio");

        perfilUsuario = Set.copyOf(perfilUsuario);
    }

}
