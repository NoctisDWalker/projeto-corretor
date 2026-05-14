package com.nerdev.auxcorretor.model;

import com.nerdev.auxcorretor.model.enums.PlanoEnum;
import com.nerdev.auxcorretor.model.enums.StatusContaEnum;
import com.nerdev.auxcorretor.model.enums.TipoContaEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "conta")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {""}) // Todo: adicionar relacionamentos
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoContaEnum tipoConta;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusContaEnum statusConta;

    @Column(unique = true)
    private String cpf;

    @Column(unique = true)
    private String cnpj;

    @Column(nullable = false, unique = true)
    private String emailResponsavel;

    @Column(nullable = false)
    private String telefoneResponsavel;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlanoEnum plano;

    @Column
    private LocalDateTime dataExpiracaoTrial;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime dataAtualizacao;

    @Column
    private LocalDateTime dataCancelamento;

}
