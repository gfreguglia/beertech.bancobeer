package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.TokenResetarSenha;
import br.com.beertechtalents.lupulo.pocmq.repository.ContaRepository;
import br.com.beertechtalents.lupulo.pocmq.repository.TokenResetarSenhaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @InjectMocks
    ContaService contaService;

    @Mock
    ContaRepository contaRepository;

    @Mock
    TokenResetarSenhaRepository tokenResetarSenhaRepository;

    @Mock
    SendMailAdapter sendMailAdapter;


    Conta conta1 = new Conta(1l, UUID.randomUUID(), "CONTA", "email@mail.com", "12345678901234",
            "wdf245l*iuga", Conta.PerfilUsuario.ADMIN, new Timestamp(100000), new ArrayList<>());

    @Test
    void getPageConta() {
        Page<Conta> pageConta = contaService.getPageConta(0, 10);
        assertThat(pageConta.getContent().get(0).getNome()).isEqualTo("CONTA");
    }

    @Test
    void novaConta() {
        Conta nova = new Conta();
        nova.setNome("NOVA CONTA");
        nova.setEmail("conta_nova@email.com");
        nova.setSenha("senha_nova");
        nova.setCnpj("");
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


    @Test
    void requestResetarSenhaEmailInvalido() {

        Mockito.when(contaService.findByEmail(Mockito.any(String.class))).then(i -> Optional.empty());

        contaService.sendRequestResetarSenha("email");
    }

    @Test
    void requestResetarSenhaEmailValido() {

        Mockito.when(contaRepository.findByEmail(Mockito.any(String.class))).then(i -> Optional.of(conta1));

        Mockito.when(tokenResetarSenhaRepository.save(Mockito.any(TokenResetarSenha.class))).
                then(i -> {
                    TokenResetarSenha mockToken = (TokenResetarSenha) i.getArguments()[0];

                    assertThat(mockToken.getExpiraEm()).isAfter(new Date());
                    assertThat(mockToken.getConta()).isEqualTo(conta1);

                    mockToken.setId(UUID.randomUUID());

                    return mockToken;
                });

        Mockito.doAnswer(i -> {
            Mail mail = (Mail) i.getArguments()[0];
            assertThat(mail.getAssunto()).isEqualTo("Requisição de mudança de senha");
            assertThat(mail.getPara()).isEqualTo(conta1.getEmail());
            return null;
        }).when(sendMailAdapter).sendMail(Mockito.any(Mail.class));

        contaService.sendRequestResetarSenha("email");
    }


    @Test
    void getContabyTokenResetarSenhaTokenInexistente() {

        Mockito.when(tokenResetarSenhaRepository.findById(Mockito.any(UUID.class))).then(i -> Optional.empty());

        Optional<Conta> optionalConta = contaService.getContabyTokenResetarSenha(UUID.randomUUID());

        assertThat(optionalConta).isEmpty();
    }

    @Test
    void getContabyTokenResetarSenhaTokenExpirado() {

        Mockito.when(tokenResetarSenhaRepository.findById(Mockito.any(UUID.class))).then(i -> {
            TokenResetarSenha tokenResetarSenha = new TokenResetarSenha(conta1);
            tokenResetarSenha.setExpiraEm(new Timestamp(1));
            return Optional.of(tokenResetarSenha);
        });

        try {
            contaService.getContabyTokenResetarSenha(UUID.randomUUID());
            fail("Erro na validacao de token");
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }


    @Test
    void getContabyTokenResetarSenhaTokenOk() {

        Mockito.when(tokenResetarSenhaRepository.findById(Mockito.any(UUID.class))).then(i -> {
            TokenResetarSenha tokenResetarSenha = new TokenResetarSenha(conta1);
            return Optional.of(tokenResetarSenha);
        });

        Optional<Conta> optionalConta = contaService.getContabyTokenResetarSenha(UUID.randomUUID());

        if (optionalConta.isPresent()) {
            assertThat(optionalConta.get()).isEqualTo(conta1);
        } else {
            fail("Empty conta");
        }

    }


}