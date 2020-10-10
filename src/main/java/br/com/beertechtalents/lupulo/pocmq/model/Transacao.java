package br.com.beertechtalents.lupulo.pocmq.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Transacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal valor;


    @ManyToOne
    @JoinColumn(
            name = "origem",
            referencedColumnName = "uuid"
    )
    private Conta origem;

    @ManyToOne
    @JoinColumn(
            name = "destino",
            referencedColumnName = "uuid"
    )
    private Conta destino;

    @CreatedDate
    private Timestamp datahora;

    public enum TipoTransacao {
        DEPOSITO,
        SAQUE
    }

}
