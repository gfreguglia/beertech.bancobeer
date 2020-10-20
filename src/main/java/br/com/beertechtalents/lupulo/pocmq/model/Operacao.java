package br.com.beertechtalents.lupulo.pocmq.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Operacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private TipoTransacao tipo;

    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private DescricaoOperacao descricaoOperacao;

    @Column(precision = 15, scale = 2, nullable = false)
    @Setter
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(
            name = "conta_uuid",
            referencedColumnName = "uuid",
            columnDefinition = "BINARY(16)"
    )

    @Setter
    private Conta conta;

    @CreatedDate
    private Timestamp datahora;

    public enum TipoTransacao {
        DEPOSITO,
        SAQUE
    }

    public enum DescricaoOperacao {
        DEPOSITO,
        SAQUE,
        TRANSFERENCIA_ORIGEM,
        TRANSFERENCIA_DESTINO,
    }

}
