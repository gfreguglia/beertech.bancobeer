package br.com.beertechtalents.lupulo.pocmq.controller;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaOperacaoDTO;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaTransferenciaDTO;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.service.ProducerService;
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
public class ProducerControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    ProducerService producerService;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void transferShouldReturnAccept() {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost")
                .port(port)
                .path("/producer/transferencia");

        NovaTransferenciaDTO dto = new NovaTransferenciaDTO();
        dto.setOrigem(UUID.randomUUID());
        dto.setDestino(UUID.randomUUID());
        dto.setValor(new BigDecimal("50.00"));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(dto, httpHeaders);

        ResponseEntity<?> responseEntity = this.restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void transferWithBodyEmptyShouldReturnError() {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost")
                .port(port)
                .path("/producer/transferencia");


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(null, httpHeaders);

        ResponseEntity<?> responseEntity = this.restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
        assertThat(responseEntity.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    public void operationShouldReturnAccept() {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost")
                .port(port)
                .path("/producer/operacao");

        NovaOperacaoDTO dto = new NovaOperacaoDTO();
        dto.setConta(UUID.randomUUID());
        dto.setTipo(Operacao.TipoTransacao.DEPOSITO);
        dto.setValor(new BigDecimal("50.00"));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(dto, httpHeaders);

        ResponseEntity<?> responseEntity = this.restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void operationWithBodyEmptyShouldReturnError() {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost")
                .port(port)
                .path("/producer/operacao");


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(null, httpHeaders);

        ResponseEntity<?> responseEntity = this.restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
        assertThat(responseEntity.getStatusCode().is4xxClientError()).isTrue();
    }

}
