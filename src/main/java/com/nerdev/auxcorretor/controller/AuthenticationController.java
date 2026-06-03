package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.dto.auth.AuthResponseDTO;
import com.nerdev.auxcorretor.dto.auth.LoginRequestDTO;
import com.nerdev.auxcorretor.dto.auth.RegistrarCorretorRequestDTO;
import com.nerdev.auxcorretor.dto.auth.RegistrarCorretorResponseDTO;
import com.nerdev.auxcorretor.model.enums.ProviderTypeEnum;
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
public class AuthenticationController {

    private final AuthService authService;
    private final RestLocationBuilder restLocationBuilder;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginDto) {

        AuthResponseDTO login = authService.login(loginDto);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/registrar/corretor")
    public ResponseEntity<RegistrarCorretorResponseDTO> registrarCorretorPadrao(
            @RequestBody @Valid RegistrarCorretorRequestDTO registrarDto) {
        ProviderTypeEnum providerType = ProviderTypeEnum.LOGIN_LOCAL;
        RegistrarCorretorResponseDTO corretorRegistradoDto =
                authService.registrarCorretor(registrarDto, providerType);
        URI location = restLocationBuilder.build(corretorRegistradoDto.usuarioId());

        return ResponseEntity.created(location).body(corretorRegistradoDto);
    }

}
