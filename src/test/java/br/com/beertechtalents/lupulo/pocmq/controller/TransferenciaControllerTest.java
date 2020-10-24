package br.com.beertechtalents.lupulo.pocmq.controller;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaTransferenciaDTO;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.repository.ContaRepository;
import br.com.beertechtalents.lupulo.pocmq.service.ContaService;
import br.com.beertechtalents.lupulo.pocmq.service.OperacaoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransferenciaControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ContaService contaService;

    @Autowired
    ContaRepository contaRepository;

    @Autowired
    OperacaoService operacaoService;

    Conta conta1 = new Conta();
    Conta conta2 = new Conta();

    @BeforeAll
    void setUp() {

        Operacao operacao = new Operacao();
        conta1.setNome("CONTA");
        conta1 = contaService.salvar(conta1);
        operacao.setConta(conta1);
        operacao.setTipo(Operacao.TipoTransacao.DEPOSITO);
        operacao.setValor(BigDecimal.valueOf(100));
        operacaoService.salvarOperacao(operacao);

        conta2.setNome("CONTA");
        conta2 = contaService.salvar(conta2);
        operacao = new Operacao();
        operacao.setConta(conta2);
        operacao.setTipo(Operacao.TipoTransacao.DEPOSITO);
        operacao.setValor(BigDecimal.valueOf(100));
    }

    @Test
    void transferShouldReturnSuccess() {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost")
                .port(port)
                .path("/transferencia");

        NovaTransferenciaDTO dto = new NovaTransferenciaDTO();
        dto.setOrigem(conta1.getUuid());
        dto.setDestino(conta2.getUuid());
        dto.setValor(new BigDecimal("50.00"));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(dto, httpHeaders);

        ResponseEntity<?> responseEntity = this.restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void transferOnUnknownOriginAccountShouldReturnError() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost")
                .port(port)
                .path("/transferencia")
                .queryParam("origem", UUID.randomUUID())
                .queryParam("destino", conta2.getUuid())
                .queryParam("valor", 100);

        ResponseEntity<?> responseEntity = this.restTemplate.exchange(builder.toUriString(), HttpMethod.POST, null, String.class);
        assertThat(responseEntity.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void transferOnUnknownDestinationAccountShouldReturnError() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost")
                .port(port)
                .path("/transferencia")
                .queryParam("origem", conta1.getUuid())
                .queryParam("destino", UUID.randomUUID())
                .queryParam("valor", 100);

        ResponseEntity<?> responseEntity = this.restTemplate.exchange(builder.toUriString(), HttpMethod.POST, null, String.class);
        assertThat(responseEntity.getStatusCode().is4xxClientError()).isTrue();
    }
}