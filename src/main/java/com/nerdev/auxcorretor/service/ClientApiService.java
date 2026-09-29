package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.ClientApi.ClientApiRequestDTO;
import com.nerdev.auxcorretor.dto.ClientApi.ClientApiResponseDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.mapper.ClientApiMapper;
import com.nerdev.auxcorretor.model.Client;
import com.nerdev.auxcorretor.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientApiService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientApiMapper clientApiMapper;

    public ClientApiResponseDTO save(ClientApiRequestDTO dto) {
       if (clientRepository.existsByClientId(dto.clientId())){
           throw new BusinessException("Client com id " + dto.clientId() + " ja existe");
       }

        String senhaCriptografada = passwordEncoder.encode(dto.clientSecret());
        Client clientApi = clientApiMapper.toEntity(dto);
        clientApi.setClientSecret(senhaCriptografada);
        Client salvo = clientRepository.save(clientApi);

        return clientApiMapper.toDto(salvo);
    }

    public Client findByClientId(String clientId) {
        return clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado"));
    }

    public Client findById(UUID id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado"));
    }

}
