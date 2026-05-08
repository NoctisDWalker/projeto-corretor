package com.nerdev.auxcorretor.model.historicos;

import com.nerdev.auxcorretor.model.Visita;
import com.nerdev.auxcorretor.model.enums.StatusVisitaEnum;
import com.nerdev.auxcorretor.model.enums.TipoHistoricoVisitaEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
@Table(name = "historico_visita")
public class HistoricoVisita {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false, name = "visita_id")
    private Visita visita;

    @Column(nullable = false, length = 1000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private StatusVisitaEnum statusVisitaAtual;

    @Column(nullable = false, updatable = false)
    private TipoHistoricoVisitaEnum tipoHistorico;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataHoraCadastro;

}
