package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class NovaOperacaoJms {
    UUID conta;
    BigDecimal valor;
}
