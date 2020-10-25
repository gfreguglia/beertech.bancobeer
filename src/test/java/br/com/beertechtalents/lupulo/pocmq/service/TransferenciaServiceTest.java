package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.Utils;
import br.com.beertechtalents.lupulo.pocmq.controller.exception.BusinessValidationException;
import br.com.beertechtalents.lupulo.pocmq.controller.exception.EntityNotFoundException;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferenciaServiceTest {

    @Mock
    ContaService contaService;

    @Mock
    OperacaoService operacaoService;

    @InjectMocks
    TransferenciaService transferenciaService;

    @Test
    void transferir() {
        Conta contaOrgigem = Utils.criarConta();
        contaOrgigem.setNome("CONTA_ORIGEM");
        contaOrgigem.setEmail("contao@email.com");
        contaOrgigem.setCnpj("12345678901244");

        Conta contaDestino = Utils.criarConta();
        contaDestino.setNome("CONTA_DESTINO");
        contaDestino.setEmail("contad@email.com");
        contaDestino.setCnpj("12345678901255");

        when(contaService.getConta(any(UUID.class))).thenReturn(Optional.of(contaOrgigem), Optional.of(contaDestino));
        transferenciaService.transferir(contaOrgigem.getUuid(), contaDestino.getUuid(), BigDecimal.valueOf(10.0));
        verify(operacaoService, times(2)).salvarOperacao(any(Operacao.class));
    }

    @Test
    void transferir_ContaOrigem() {
        when(contaService.getConta(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            transferenciaService.transferir(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(10.0));;
        });
    }

    @Test
    void transferir_ContaDestino() {
        when(contaService.getConta(any(UUID.class))).thenReturn(Optional.of(Utils.criarConta()), Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            transferenciaService.transferir(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(10.0));;
        });
    }
}