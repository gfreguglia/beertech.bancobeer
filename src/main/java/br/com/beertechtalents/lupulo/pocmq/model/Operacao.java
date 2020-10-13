package br.com.beertechtalents.lupulo.pocmq.model;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Operacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(
            name = "conta_uuid",
            referencedColumnName = "uuid",
            columnDefinition = "BINARY(16)"
    )
    private Conta conta;

    @CreatedDate
    @Getter
    private Timestamp datahora;

    public enum TipoTransacao {
        DEPOSITO,
        SAQUE
    }

}
