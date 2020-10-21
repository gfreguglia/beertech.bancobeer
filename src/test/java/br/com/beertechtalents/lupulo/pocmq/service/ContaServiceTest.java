package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.repository.ContaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ContaServiceTest {

    @Autowired
    ContaService contaService;

    @Autowired
    ContaRepository contaRepository;


    Conta conta1 = new Conta();

    @BeforeAll
    void setUp() {
        conta1.setNome("CONTA");
        conta1.setEmail("conta3@email.com");
        conta1.setSenha("senha");
        conta1.setCnpj("12345678901235");
        conta1.setPerfil(Conta.PerfilUsuario.ADMIN);
        conta1 = contaRepository.save(conta1);
    }

    @Test
    void getPageConta() {
        Page<Conta> pageConta = contaService.getPageConta(0, 10);
        assertThat(pageConta.getContent().get(0).getNome()).isEqualTo("CONTA");
    }

    @Test
    void novaConta() {
        Conta nova = new Conta();
        nova.setNome("NOVA CONTA");
        nova.setEmail("conta_nova3@email.com");
        nova.setSenha("senha_nova");
        nova.setCnpj("12345678904326");
        nova.setPerfil(Conta.PerfilUsuario.USER);
        nova = contaService.novaConta(nova);
        assertThat(nova.getId()).isGreaterThan(0);
    }

    @Test
    void getConta() {
        Optional<Conta> conta = contaService.getConta(conta1.getUuid());
        assertThat(conta.isPresent()).isTrue();
    }

    @Test
    void computeSaldo() {
        assertThat(contaService.computeSaldo(conta1)).isEqualTo(BigDecimal.valueOf(0.0));
    }


}