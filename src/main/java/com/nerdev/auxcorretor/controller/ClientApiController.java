package com.nerdev.auxcorretor.controller;

import com.nerdev.auxcorretor.dto.ClientApi.ClientApiRequestDTO;
import com.nerdev.auxcorretor.dto.ClientApi.ClientApiResponseDTO;
import com.nerdev.auxcorretor.service.ClientApiService;
import com.nerdev.auxcorretor.web.util.RestLocationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ClientApi")
public class ClientApiController {

    private final ClientApiService clientApiService;
    private final RestLocationBuilder restLocationBuilder;

    @PostMapping
    public ResponseEntity<ClientApiResponseDTO> save(@RequestBody ClientApiRequestDTO dto) {
        ClientApiResponseDTO salvo = clientApiService.save(dto);
        URI location = restLocationBuilder.build(salvo.id());
        return ResponseEntity.created(location).body(salvo);
    }
}
