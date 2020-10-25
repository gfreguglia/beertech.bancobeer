package br.com.beertechtalents.lupulo.pocmq;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Utils {

  public static Operacao criarOperacaoDefault() {
    Operacao op = new Operacao();
    op.setValor(BigDecimal.TEN);
    op.setTipo(Operacao.TipoTransacao.DEPOSITO);
    op.setConta(criarConta());
    return op;
  }

  public static Conta criarConta() {
    Conta conta = new Conta();
    conta.setNome("CONTA");
    conta.setEmail("conta@email.com");
    conta.setSenha("senha");
    conta.setCnpj("12345678901234");
    conta.setCriadoEm(Timestamp.valueOf(LocalDateTime.now()));
    return conta;
  }


}
