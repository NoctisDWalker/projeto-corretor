package com.nerdev.auxcorretor.dto.exception;

import java.util.List;

public record ErroResposta(int status, String mensagem, List<ErroCampo> erros) {
}
