    package com.nerdev.auxcorretor.service;

    import com.nerdev.auxcorretor.dto.conta.ContaCreateRequestDTO;
    import com.nerdev.auxcorretor.dto.conta.ContaResponseDTO;
    import com.nerdev.auxcorretor.dto.conta.ContaUpdateRequestDTO;
    import com.nerdev.auxcorretor.exception.BusinessException;
    import com.nerdev.auxcorretor.mapper.ContaMapper;
    import com.nerdev.auxcorretor.model.Conta;
    import com.nerdev.auxcorretor.model.enums.StatusContaEnum;
    import com.nerdev.auxcorretor.repository.ContaRepository;
    import com.nerdev.auxcorretor.validation.ContaValidator;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;
    import java.util.UUID;

    @Service
    @RequiredArgsConstructor
    public class ContaService {

        private final ContaMapper contaMapper;
        private final ContaValidator contaValidator;
        private final ContaRepository contaRepository;

        public ContaResponseDTO criarConta(ContaCreateRequestDTO createDto) {

            Conta entity = contaMapper.toEntity(createDto);
            contaValidator.validaCadastro(entity);

            validarStatusInicial(entity.getStatusConta());
            entity.setDataExpiracao(dataExpiracaoPlano());
            Conta salvo = contaRepository.save(entity);

            return contaMapper.toDto(salvo);
        }

        private void validarStatusInicial(StatusContaEnum status) {
            if (status.acessoBloqueado()){
                throw new BusinessException("Status invalido para criação!");
            }
        }

        private LocalDateTime dataExpiracaoPlano(){
            // Todo: Definir posteriormente regra para data de expiração
            return LocalDateTime.now().plusDays(30);
        }

        public ContaResponseDTO atualizar(UUID contaId, ContaUpdateRequestDTO updateDto) {
            Conta contaEncontrada = contaRepository.findById(contaId)
                    .orElseThrow(() -> new BusinessException("Conta não encontrada"));

            contaValidator.validarContaOperacional(contaEncontrada);
            contaMapper.updateEntity(updateDto, contaEncontrada);

            Conta salva = contaRepository.save(contaEncontrada);

            return contaMapper.toDto(salva);
        }

    }
