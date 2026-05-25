package com.nerdev.auxcorretor.model;

import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.model.enums.PlanoEnum;
import com.nerdev.auxcorretor.model.enums.StatusContaEnum;
import com.nerdev.auxcorretor.model.enums.TipoContaEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "conta")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"usuarios", "corretorContas"})
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Builder.Default
    @OneToMany(mappedBy = "conta", fetch = FetchType.LAZY)
    private Set<Usuario> usuarios = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "conta", fetch = FetchType.LAZY)
    private Set<CorretorConta> corretorContas = new HashSet<>();

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

    public void adicionarCorretorConta(CorretorConta corretorConta) {
        if (corretorConta == null){
            throw new BusinessException("CorretorConta não pode ser nulo");
        }

        boolean existeCorretor = this.corretorContas.stream()
                .anyMatch(c -> c.getCorretor().getId().equals(corretorConta.getCorretor().getId()));
        if (existeCorretor){
            throw new BusinessException("Corretor já vinculado a esta conta.");
        }

        corretorConta.setConta(this);
        this.corretorContas.add(corretorConta);
    }

    public void removerCorretorConta(CorretorConta corretorConta) {
        if(!this.corretorContas.contains(corretorConta)){
            throw new BusinessException("CorretorConta não encontrado");
        }

        this.corretorContas.remove(corretorConta);
        corretorConta.setConta(null);
    }

}
