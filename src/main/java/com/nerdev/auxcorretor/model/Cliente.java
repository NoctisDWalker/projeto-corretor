package com.nerdev.auxcorretor.model;

import com.nerdev.auxcorretor.model.enums.StatusClienteEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column
    private String telefone;

    @Column
    private String email;

    @Column
    private String cpf;

    @Column
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusClienteEnum statusCliente;

    @Column(nullable = false)
    @CreatedDate
    private LocalDate dataCadastro;

    @Column
    @LastModifiedDate
    private LocalDate dataAtualizacao;

}
