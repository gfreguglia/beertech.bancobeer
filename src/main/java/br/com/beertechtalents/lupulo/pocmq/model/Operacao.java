package br.com.beertechtalents.lupulo.pocmq.model;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Data
public class Operacao {

    @Indexed
    private TipoTransacao tipo;

    private BigDecimal valor;

    private Date datahora = new Date(Instant.now().toEpochMilli());

    public enum TipoTransacao {
        DEPOSITO,
        SAQUE
    }

}
