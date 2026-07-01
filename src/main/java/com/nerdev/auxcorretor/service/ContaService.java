    package com.nerdev.auxcorretor.service;

    import com.nerdev.auxcorretor.dto.conta.ContaCreateRequestDTO;
    import com.nerdev.auxcorretor.dto.conta.ContaResponseDTO;
    import com.nerdev.auxcorretor.exception.BusinessException;
    import com.nerdev.auxcorretor.mapper.ContaMapper;
    import com.nerdev.auxcorretor.model.Conta;
    import com.nerdev.auxcorretor.model.enums.StatusContaEnum;
    import com.nerdev.auxcorretor.repository.ContaRepository;
    import com.nerdev.auxcorretor.validation.ContaValidator;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;

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

    }
