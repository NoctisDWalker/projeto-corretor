package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.controller.docs.AuthenticationDoc;
import com.nerdev.auxcorretor.dto.auth.RegistrarCorretorRequestDTO;
import com.nerdev.auxcorretor.dto.auth.RegistrarCorretorResponseDTO;
import com.nerdev.auxcorretor.model.enums.ProviderTypeEnum;
import com.nerdev.auxcorretor.security.annotations.permissions.authentication.PodeCadastrarCorretorIndividual;
import com.nerdev.auxcorretor.service.AuthService;
import com.nerdev.auxcorretor.web.util.RestLocationBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController implements AuthenticationDoc {

    private final AuthService authService;
    private final RestLocationBuilder restLocationBuilder;

    @Override
    @PostMapping("/registrar/corretor")
    @PodeCadastrarCorretorIndividual
    public ResponseEntity<RegistrarCorretorResponseDTO> registrarCorretorIndividual(
            @RequestBody @Valid RegistrarCorretorRequestDTO registrarDto) {
        ProviderTypeEnum providerType = ProviderTypeEnum.LOGIN_LOCAL;
        RegistrarCorretorResponseDTO corretorRegistradoDto =
                authService.registrarCorretorIndividual(registrarDto);
        URI location = restLocationBuilder.build(corretorRegistradoDto.credencialUsuario().id());

        return ResponseEntity.created(location).body(corretorRegistradoDto);
    }

}
