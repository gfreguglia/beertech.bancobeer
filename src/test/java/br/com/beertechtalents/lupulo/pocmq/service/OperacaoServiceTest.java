package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.repository.OperacaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;

@ExtendWith(MockitoExtension.class)
public class OperacaoServiceTest {

    @InjectMocks
    private OperacaoService operacaoService;

    @Mock
    private OperacaoRepository operacaoRepository;

    @Test
    public void saveTest() {
        Operacao operacao = new Operacao();
        operacao.setTipo(Operacao.TipoTransacao.DEPOSITO);
        operacao.setValor(BigDecimal.TEN);


        Mockito.when(operacaoRepository.save(Mockito.any(Operacao.class))).then(i -> {
            Operacao t = (Operacao) i.getArguments()[0];
            t.setId(1L);
            t.setDatahora(new Timestamp(10000L));
            return t;
        });

        operacaoService.salvarOperacao(operacao);
    }


    @Test
    public void buscarSaldoTest() {

        Mockito.when(operacaoRepository.somaSaldo()).then(i -> BigDecimal.TEN);

        BigDecimal saldo = operacaoService.buscarSaldo();

        Assertions.assertEquals(BigDecimal.TEN, saldo);
    }

}
