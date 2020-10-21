package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransferenciaServiceTest {

    @Autowired
    ContaService contaService;

    @Autowired
    OperacaoService operacaoService;

    @Autowired
    TransferenciaService transferenciaService;

    Conta conta1 = new Conta();
    Conta conta2 = new Conta();

    @BeforeAll
    void setUp() {
        conta1.setNome("ORIGEM");
        conta1.setEmail("conta1@email.com");
        conta1.setSenha("senha");
        conta1.setCnpj("12345678901238");
        conta1.setPerfil(Conta.PerfilUsuario.ADMIN);

        conta2.setNome("DESTINO");
        conta2.setEmail("destino1@email.com");
        conta2.setSenha("destino");
        conta2.setCnpj("12345678904329");
        conta2.setPerfil(Conta.PerfilUsuario.ADMIN);

        conta1 = contaService.novaConta(conta1);
        conta2 = contaService.novaConta(conta2);

        Operacao op = new Operacao();
        op.setConta(conta1);
        op.setValor(BigDecimal.valueOf(100.0));
        op.setTipo(Operacao.TipoTransacao.DEPOSITO);
        op.setDescricaoOperacao(Operacao.DescricaoOperacao.DEPOSITO);

        operacaoService.salvarOperacao(op);

    }

    @Test
    void transferir() {
        transferenciaService.transferir(conta1.getUuid(), conta2.getUuid(), BigDecimal.TEN);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private void conferirSaldo() {
        conta1 = contaService.getConta(conta1.getUuid()).orElse(null);
        conta2 = contaService.getConta(conta2.getUuid()).orElse(null);

        BigDecimal saldo1 = contaService.computeSaldo(conta1);
        BigDecimal saldo2 = contaService.computeSaldo(conta2);

        assertThat(saldo1.compareTo(new BigDecimal("90.0"))).isEqualTo(0);
        assertThat(saldo2.compareTo(new BigDecimal("10.0"))).isEqualTo(0);
    }
}