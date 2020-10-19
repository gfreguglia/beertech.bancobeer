package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class ConsultaExtratoDTO {
    final Long id;
    final Operacao.TipoTransacao tipo;
    final BigDecimal valor;
}
