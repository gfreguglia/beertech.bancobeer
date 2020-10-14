package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class NovaOperacaoDTO {
    UUID conta;
    Operacao.TipoTransacao tipo;
    BigDecimal valor;
}
