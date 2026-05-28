package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.dto.auth.AuthResponseDTO;
import com.nerdev.auxcorretor.dto.auth.LoginRequestDTO;
import com.nerdev.auxcorretor.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthLoginController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginDto){

        AuthResponseDTO login = authService.login(loginDto);
        return ResponseEntity.ok(login);
    }


}
