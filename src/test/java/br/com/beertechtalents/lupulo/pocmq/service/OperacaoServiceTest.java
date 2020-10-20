package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.repository.ContaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OperacaoServiceTest {

    @Autowired
    ContaRepository contaRepository;

    Conta conta = new Conta();

    @BeforeAll
    void setUp() {
        conta.setNome("CONTA");
        conta = contaRepository.save(conta);
    }

    @Test
    void salvarOperacao() {
        Operacao op = new Operacao();
        op.setValor(BigDecimal.valueOf(10.0));
        op.setTipo(Operacao.TipoTransacao.DEPOSITO);
//        op.setConta(conta);
//        operacaoService.salvarOperacao(op);
    }
}