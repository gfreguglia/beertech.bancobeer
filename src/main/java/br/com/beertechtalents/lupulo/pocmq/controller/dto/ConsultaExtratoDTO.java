package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@JsonInclude(Include.NON_NULL)
public class ConsultaExtratoDTO {
    final Long id;
    final Operacao.TipoTransacao tipo;
    final Operacao.DescricaoOperacao descricaoOperacao;
    final BigDecimal valor;
    final String categoria;
    final Timestamp timestamp;
    final BigDecimal saldoAtual;
}
