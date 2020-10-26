package br.com.beertechtalents.lupulo.pocmq.controller;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaTransferenciaDTO;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.repository.ContaRepository;
import br.com.beertechtalents.lupulo.pocmq.service.ContaService;
import br.com.beertechtalents.lupulo.pocmq.service.OperacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransferenciaControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    ContaService contaService;

    @Autowired
    ContaRepository contaRepository;

    @Autowired
    OperacaoService operacaoService;

    @Autowired
    PasswordEncoder passwordEncoder;

    Conta conta1 = new Conta();
    Conta conta2 = new Conta();

    @BeforeAll
    void setUp() {

        Operacao operacao = new Operacao();
        conta1.setNome("CONTA");
        conta1.setEmail("teste@teste.com");
        conta1.setCnpj("11111111111111");
        conta1.setSenha(passwordEncoder.encode("123"));
        conta1 = contaService.salvar(conta1);
        operacao.setConta(conta1);
        operacao.setTipo(Operacao.TipoTransacao.DEPOSITO);
        operacao.setValor(BigDecimal.valueOf(100));
        operacao.setDescricaoOperacao(Operacao.DescricaoOperacao.DEPOSITO);
        operacaoService.salvarOperacao(operacao);

        conta2.setNome("CONTA2");
        conta2.setEmail("teste2@teste.com");
        conta2.setCnpj("11111111111112");
        conta2.setSenha(passwordEncoder.encode("123"));
        conta2 = contaService.salvar(conta2);
        operacao = new Operacao();
        operacao.setConta(conta2);
        operacao.setTipo(Operacao.TipoTransacao.DEPOSITO);
        operacao.setValor(BigDecimal.valueOf(100));
        operacao.setDescricaoOperacao(Operacao.DescricaoOperacao.DEPOSITO);
        operacaoService.salvarOperacao(operacao);

        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void transferShouldReturnSuccess() throws Exception {

        SecurityContextHolder.getContext()
                .setAuthentication(new PreAuthenticatedAuthenticationToken(null,
                        null,
                        Arrays.asList(new SimpleGrantedAuthority(conta1.getUuid().toString()))));

        NovaTransferenciaDTO dto = new NovaTransferenciaDTO();
        dto.setOrigem(conta1.getUuid());
        dto.setDestino(conta2.getUuid());
        dto.setValor(new BigDecimal("50.00"));

        ObjectMapper mapper = new ObjectMapper();
        mvc.perform(post("/transferencia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void transferOnUnknownOriginAccountShouldReturnError() throws Exception {
        UUID random = UUID.randomUUID();
        SecurityContextHolder.getContext()
                .setAuthentication(new PreAuthenticatedAuthenticationToken(null,
                        null,
                        Arrays.asList(new SimpleGrantedAuthority(random.toString()))));

        mvc.perform(post("/transferencia",
                "origem", random,
                "destino", conta2.getUuid(),
                "valor", 100))
                .andExpect(status().isBadRequest());
    }

    @Test
    void transferOnUnknownDestinationAccountShouldReturnError() throws Exception {
        SecurityContextHolder.getContext()
                .setAuthentication(new PreAuthenticatedAuthenticationToken(null,
                        null,
                        Arrays.asList(new SimpleGrantedAuthority(conta1.getUuid().toString()))));

        mvc.perform(post("/transferencia",
                "origem", conta1.getUuid(),
                "destino", UUID.randomUUID(),
                "valor", 100))
                .andExpect(status().isBadRequest());
    }
}