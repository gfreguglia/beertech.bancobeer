package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.model.TipoTransacao;
import br.com.beertechtalents.lupulo.pocmq.model.Transacao;
import br.com.beertechtalents.lupulo.pocmq.repository.TransacaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.sql.Timestamp;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    @InjectMocks
    private TransacaoService transacaoService;

    @Mock
    private TransacaoRepository transacaoRepository;

    @Test
    public void saveTest() {
        Transacao transacao = new Transacao();
        transacao.setTipo(TipoTransacao.DEPOSITO);
        transacao.setValor(BigDecimal.TEN);


        Mockito.when(transacaoRepository.save(Mockito.any(Transacao.class))).then(i -> {
            Transacao t = (Transacao) i.getArguments()[0];
            t.setId(1l);
            t.setDatahora(new Timestamp(10000l));
            return t;
        });

        transacaoService.salvarTransacao(transacao);
    }


    @Test
    public void buscarSaldoTest() {

        Mockito.when(transacaoRepository.somaSaldo()).then(i -> BigDecimal.TEN);

        BigDecimal saldo = transacaoService.buscarSaldo();

        Assertions.assertEquals(BigDecimal.TEN, saldo);
    }

}
