package br.com.beertechtalents.lupulo.pocmq.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Table(name = "transacoes")
@Entity
@Data
public class Transacao {

    @Id
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    @JsonProperty("tipo")
    private TipoTransacao tipo;

    @JsonProperty("valor")
    private BigDecimal valor;

    @CreationTimestamp
    @Column(name="datahora", nullable = false, updatable = false, insertable = false)
    private Timestamp datahora;

}
