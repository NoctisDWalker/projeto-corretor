package com.nerdev.auxcorretor.model;

import com.nerdev.auxcorretor.model.enums.InteresseClienteEnum;
import com.nerdev.auxcorretor.model.enums.StatusVisitaEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString(exclude = {"atendimento", "imovel"})
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(
        indexes = {
                @Index(name = "idx_visita_atendimento", columnList = "atendimento_id"),
                @Index(name = "idx_visita_imovel", columnList = "imovel_id"),
                @Index(name = "idx_visita_data", columnList = "dataHoraAgendada")
        }
)
public class Visita {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "atendimento_id",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Atendimento atendimento;

    @JoinColumn(name = "imovel_id",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Imovel imovel;

    @Column(nullable = false)
    private LocalDateTime dataHoraAgendada;

    @Column
    private LocalDateTime dataHoraRealizada;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusVisitaEnum statusVisita;

    @Column
    @Enumerated(EnumType.STRING)
    private InteresseClienteEnum interesseCliente;

    @Column
    private String observacoes;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate dataCadastro;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDate dataAtualizacao;

}
