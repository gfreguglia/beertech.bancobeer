package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.repository.ContaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.xml.sax.helpers.AttributesImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OperacaoServiceTest {

    @Autowired
    ContaRepository contaRepository;

    @Autowired
    OperacaoService operacaoService;

    Conta conta = new Conta();

    @BeforeAll
    void setUp() {
        conta.setNome("CONTA");
        conta.setEmail("conta@email.com");
        conta.setSenha("senha");
        conta.setCnpj(BigInteger.TEN);
        conta.setPerfil(Conta.PerfilUsuario.ADMIN);
        conta = contaRepository.save(conta);
    }

    @Test
    @WithMockUser(username = "conta@email.com", password = "senha", authorities = "ADMIN")
    void salvarOperacao() {
        Operacao op = new Operacao();
        op.setValor(BigDecimal.valueOf(10.0));
        op.setTipo(Operacao.TipoTransacao.DEPOSITO);
        op.setConta(conta);
        operacaoService.salvarOperacao(op);
    }

    @Test
    @WithMockUser(username = "conta@email.com", password = "senha", authorities = "ADMIN")
    void getOperacao() {
        salvarOperacao();

        Page<Operacao> pageOperacao = operacaoService.getPageOperacao(conta.getUuid(), 0, 10);
        assertThat(pageOperacao.getContent().get(0).getTipo()).isEqualTo(Operacao.TipoTransacao.DEPOSITO);
    }

    @Test
    @WithMockUser(username = "conta@email.com", password = "senha", authorities = "ADMIN")
    void getExtrato() {
        salvarOperacao();
        Timestamp init = conta.getCriadoEm();
        Timestamp end = new Timestamp(System.currentTimeMillis());

        Page<Operacao> pageOperacao = operacaoService.getPageExtrato(conta.getUuid(), init, end, 0, 10);
        assertThat(pageOperacao.getContent().get(0).getTipo()).isEqualTo(Operacao.TipoTransacao.DEPOSITO);

        Page<Operacao> pageOperacao1 = operacaoService.getPageExtrato(conta.getUuid(), init, init, 0, 10);
        assertThat(pageOperacao1.getContent().isEmpty()).isTrue();
    }
}