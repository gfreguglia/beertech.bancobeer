package br.com.beertechtalents.lupulo.pocmq.controller;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.DadosUsuarioSessao;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.JwtRequest;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.JwtResponse;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaTransferenciaDTO;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.repository.ContaRepository;
import br.com.beertechtalents.lupulo.pocmq.service.ContaService;
import br.com.beertechtalents.lupulo.pocmq.service.OperacaoService;
import br.com.beertechtalents.lupulo.pocmq.util.JwtTokenUtil;
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
    OperacaoService operacaoService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    Conta conta1 = new Conta();
    Conta conta2 = new Conta();

    HttpHeaders httpHeaders;

    @BeforeAll
    void setUp() {

        Operacao operacao = new Operacao();
        conta1.setNome("CONTA");
        conta1.setEmail("conta4@email.com");
        conta1.setSenha("senha");
        conta1.setCnpj("12345678901233");
        conta1.setPerfil(Conta.PerfilUsuario.ADMIN);
        conta1 = contaService.novaConta(conta1);
        operacao.setConta(conta1);
        operacao.setTipo(Operacao.TipoTransacao.DEPOSITO);
        operacao.setDescricaoOperacao(Operacao.DescricaoOperacao.DEPOSITO);
        operacao.setValor(BigDecimal.valueOf(100));
        operacaoService.salvarOperacao(operacao);

        conta2.setNome("CONTA2");
        conta2.setEmail("conta7@email.com");
        conta2.setSenha("senha2");
        conta2.setCnpj("12345678904321");
        conta2.setPerfil(Conta.PerfilUsuario.ADMIN);
        conta2 = contaService.novaConta(conta2);
        operacao = new Operacao();
        operacao.setConta(conta2);
        operacao.setTipo(Operacao.TipoTransacao.DEPOSITO);
        operacao.setDescricaoOperacao(Operacao.DescricaoOperacao.DEPOSITO);
        operacao.setValor(BigDecimal.valueOf(100));

        DadosUsuarioSessao userDetails = new DadosUsuarioSessao(conta1.getEmail(), "", conta1.getAuthorities(),
                conta1.getEmail(), conta1.getNome(), conta1.getCnpj(), conta1.getPerfil());
        String token = jwtTokenUtil.generateToken(userDetails);

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);
    }

    @Test
    public void transferShouldReturnSuccess() {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost")
                .port(port)
                .path("/transferencia");

        NovaTransferenciaDTO dto = new NovaTransferenciaDTO();
        dto.setOrigem(conta1.getUuid());
        dto.setDestino(conta2.getUuid());
        dto.setValor(new BigDecimal("50.00"));

        HttpEntity entity = new HttpEntity(dto, httpHeaders);

        ResponseEntity<?> responseEntity = this.restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void transferOnUnknownOriginAccountShouldReturnError() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost")
                .port(port)
                .path("/transferencia")
                .queryParam("origem", UUID.randomUUID())
                .queryParam("destino", conta2.getUuid())
                .queryParam("valor", 100);

        HttpEntity entity = new HttpEntity(httpHeaders);

        ResponseEntity<?> responseEntity = this.restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
        assertThat(responseEntity.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    public void transferOnUnknownDestinationAccountShouldReturnError() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost")
                .port(port)
                .path("/transferencia")
                .queryParam("origem", conta1.getUuid())
                .queryParam("destino", UUID.randomUUID())
                .queryParam("valor", 100);

        HttpEntity entity = new HttpEntity(httpHeaders);

        ResponseEntity<?> responseEntity = this.restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
        assertThat(responseEntity.getStatusCode().is4xxClientError()).isTrue();
    }
}