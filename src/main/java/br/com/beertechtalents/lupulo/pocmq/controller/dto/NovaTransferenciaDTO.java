package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import lombok.Data;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class NovaTransferenciaDTO {
    UUID origem;
    UUID destino;

    @Positive
    BigDecimal valor;
}
