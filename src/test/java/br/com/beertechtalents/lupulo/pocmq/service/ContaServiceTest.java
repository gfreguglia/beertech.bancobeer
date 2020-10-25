package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.events.EventPublisher;
import br.com.beertechtalents.lupulo.pocmq.events.OutboxEvent;
import br.com.beertechtalents.lupulo.pocmq.events.RequestChangePasswordEvents;
import br.com.beertechtalents.lupulo.pocmq.exception.TokenInvalidException;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.TokenTrocarSenha;
import br.com.beertechtalents.lupulo.pocmq.repository.ContaRepository;
import br.com.beertechtalents.lupulo.pocmq.repository.TokenResetarSenhaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

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
    PasswordEncoder passwordEncoder;

    @Mock
    EventPublisher eventPublisher;

    @Mock
    RequestChangePasswordEvents requestChangePasswordEvents;


    Conta conta1 = new Conta(1L, UUID.randomUUID(), "CONTA", "email@mail.com", "12345678901234",
            "wdf245l*iuga", new Timestamp(100000), new ArrayList<>());

    //@Test
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
        nova.setCnpj("11111111111111");

        Mockito.when(contaRepository.save(Mockito.any(Conta.class))).then(i -> i.getArguments()[0]);

        nova = contaService.salvar(nova);

        assertThat(nova).isNotNull();
    }

    @Test
    void getContaUuid() {

        Mockito.when(contaRepository.findByUuid(Mockito.any(UUID.class))).then(i -> Optional.of(conta1));

        Optional<Conta> conta = contaService.getConta(conta1.getUuid());

        assertThat(conta).isPresent();
    }

    @Test
    void computeSaldo() {
        assertThat(contaService.computeSaldo(conta1)).isEqualTo(BigDecimal.valueOf(0.0));
    }


    @Test
    void requestResetarSenhaEmailInvalido() {

        Mockito.when(contaService.findByEmail(Mockito.any(String.class))).then(i -> Optional.empty());
        Assertions.assertDoesNotThrow(() -> contaService.sendRequestTrocarSenha("email"));
    }

    @Test
    void requestResetarSenhaEmailValido() {
        Long tokenId = 37L;

        Mockito.when(contaRepository.findByEmail(Mockito.any(String.class))).then(i -> Optional.of(conta1));

        Mockito.when(tokenResetarSenhaRepository.save(Mockito.any(TokenTrocarSenha.class))).
                then(i -> {
                    TokenTrocarSenha mockToken = (TokenTrocarSenha) i.getArguments()[0];


                    assertThat(mockToken.getExpiraEm()).isAfter(new Date());
                    assertThat(mockToken.getConta()).isEqualTo(conta1);

                    ReflectionTestUtils.setField(mockToken, "uuid", UUID.randomUUID());
                    ReflectionTestUtils.setField(mockToken, "id", tokenId);

                    return mockToken;
                });


        Mockito.when(requestChangePasswordEvents.createRequestChangePasswordEvents(Mockito.any(TokenTrocarSenha.class))).
                then(i -> new OutboxEvent(1L, null, null));
        Mockito.doNothing().when(eventPublisher).fire(Mockito.any(OutboxEvent.class));


        contaService.sendRequestTrocarSenha("email");
    }


    @Test
    void getContabyTokenResetarSenhaTokenInexistente() {
        Mockito.when(tokenResetarSenhaRepository.findByUuid(Mockito.any(UUID.class))).then(i -> Optional.empty());
        UUID uuid = UUID.randomUUID();
        Assertions.assertThrows(TokenInvalidException.class, () -> contaService.getContabyTokenTrocarSenha(uuid));
    }

    @Test
    void getContabyTokenResetarSenhaTokenExpirado() {

        Mockito.when(tokenResetarSenhaRepository.findByUuid(Mockito.any(UUID.class))).then(i -> {
            TokenTrocarSenha tokenResetarSenha = new TokenTrocarSenha(conta1);
            ReflectionTestUtils.setField(tokenResetarSenha, "expiraEm", new Timestamp(1));
            return Optional.of(tokenResetarSenha);
        });
        UUID uuid = UUID.randomUUID();
        Assertions.assertThrows(TokenInvalidException.class, () -> contaService.getContabyTokenTrocarSenha(uuid));
    }

    @Test
    void getContabyTokenResetarSenhaTokenUsado() {

        Mockito.when(tokenResetarSenhaRepository.findByUuid(Mockito.any(UUID.class))).then(i -> {
            TokenTrocarSenha tokenResetarSenha = new TokenTrocarSenha(conta1);
            tokenResetarSenha.invalidar();
            return Optional.of(tokenResetarSenha);
        });

        UUID uuid = UUID.randomUUID();
        Assertions.assertThrows(TokenInvalidException.class, () -> contaService.getContabyTokenTrocarSenha(uuid));

    }


    @Test
    void getContabyTokenResetarSenhaTokenOk() {

        Mockito.when(tokenResetarSenhaRepository.findByUuid(Mockito.any(UUID.class))).then(i -> {
            TokenTrocarSenha tokenResetarSenha = new TokenTrocarSenha(conta1);
            return Optional.of(tokenResetarSenha);
        });

        UUID uuid = UUID.randomUUID();
        Optional<Conta> optionalConta = contaService.getContabyTokenTrocarSenha(uuid);

        if (optionalConta.isPresent()) {
            assertThat(optionalConta).contains(conta1);
        } else {
            fail("Empty conta");
        }
    }


    @Test
    void trocaSenhaTokenExpirado() {
        Mockito.when(tokenResetarSenhaRepository.findByUuid(Mockito.any(UUID.class))).then(i -> {
            TokenTrocarSenha tokenResetarSenha = new TokenTrocarSenha(conta1);
            ReflectionTestUtils.setField(tokenResetarSenha, "expiraEm", new Timestamp(1));
            return Optional.of(tokenResetarSenha);
        });

        UUID uuid = UUID.randomUUID();
        UUID contaId = conta1.getUuid();
        Assertions.assertThrows(TokenInvalidException.class, () -> contaService.trocarSenha(contaId, "nova senha", uuid));
    }

    @Test
    void trocaSenhaTokenUsado() {
        Mockito.when(tokenResetarSenhaRepository.findByUuid(Mockito.any(UUID.class))).then(i -> {
            TokenTrocarSenha tokenResetarSenha = new TokenTrocarSenha(conta1);
            tokenResetarSenha.invalidar();
            return Optional.of(tokenResetarSenha);
        });

        UUID uuid = UUID.randomUUID();
        UUID contaId = conta1.getUuid();
        Assertions.assertThrows(TokenInvalidException.class, () -> contaService.trocarSenha(contaId, "nova senha", uuid));

    }

    @Test
    void trocaSenhaTokenInexistente() {
        Mockito.when(tokenResetarSenhaRepository.findByUuid(Mockito.any(UUID.class))).then(i -> Optional.empty());

        UUID uuid = UUID.randomUUID();
        UUID contaId = conta1.getUuid();
        Assertions.assertThrows(TokenInvalidException.class, () -> contaService.trocarSenha(contaId, "nova senha", uuid));
    }

    @Test
    void trocaSenhaTokenOutraConta() {
        Mockito.when(tokenResetarSenhaRepository.findByUuid(Mockito.any(UUID.class))).then(i -> {
            Conta outraConta = new Conta();
            outraConta.setId(2L);
            outraConta.setEmail("outro@mail.com");
            outraConta.setSenha("outra senha");

            TokenTrocarSenha tokenResetarSenha = new TokenTrocarSenha(outraConta);
            return Optional.of(tokenResetarSenha);
        });

        UUID uuid = UUID.randomUUID();
        UUID contaId = conta1.getUuid();
        Assertions.assertThrows(TokenInvalidException.class, () -> contaService.trocarSenha(contaId, "nova senha", uuid));

    }


    @Test
    void trocaSenhaTokenOK() {
        Mockito.when(tokenResetarSenhaRepository.findByUuid(Mockito.any(UUID.class))).then(i -> {
            TokenTrocarSenha tokenResetarSenha = new TokenTrocarSenha(conta1);
            return Optional.of(tokenResetarSenha);
        });

        Mockito.when(tokenResetarSenhaRepository.save(Mockito.any(TokenTrocarSenha.class))).then(i -> {
            TokenTrocarSenha tokenResetarSenha = (TokenTrocarSenha) i.getArguments()[0];
            assertThat(tokenResetarSenha.isInvalido()).isTrue();
            return tokenResetarSenha;
        });

        Mockito.when(passwordEncoder.encode(Mockito.any(String.class))).then(i -> {
            String senha = (String) i.getArguments()[0];

            assertThat(senha).isEqualTo("nova senha");
            return senha;
        });

        Mockito.when(contaRepository.save(Mockito.any(Conta.class))).then(i -> conta1);

        contaService.trocarSenha(conta1.getUuid(), "nova senha", UUID.randomUUID());
    }


}