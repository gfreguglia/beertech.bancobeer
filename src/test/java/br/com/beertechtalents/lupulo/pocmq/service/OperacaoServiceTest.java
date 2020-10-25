package br.com.beertechtalents.lupulo.pocmq.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.beertechtalents.lupulo.pocmq.Utils;
import br.com.beertechtalents.lupulo.pocmq.controller.exception.BusinessValidationException;
import br.com.beertechtalents.lupulo.pocmq.events.EventPublisher;
import br.com.beertechtalents.lupulo.pocmq.events.OutboxEvent;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao.TipoTransacao;
import br.com.beertechtalents.lupulo.pocmq.repository.ContaRepository;
import br.com.beertechtalents.lupulo.pocmq.repository.OperacaoRepository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class OperacaoServiceTest {

  @InjectMocks
  private OperacaoService operacaoService;

  @Mock
  private ContaRepository contaRepository;

  @Mock
  private ContaService contaService;

  @Mock
  private EventPublisher eventPublisher;

  @Mock
  private OperacaoRepository operacaoRepository;


  @Test
  void salvarOperacaoDeposito() {
    Operacao op = Utils.criarOperacaoDefault();
    when(contaRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(op.getConta()));
    when(contaService.computeSaldo(any(Conta.class))).thenReturn(BigDecimal.TEN);
    operacaoService.salvarOperacao(op);
    verify(operacaoRepository, times(1)).save(any(Operacao.class));
    verify(eventPublisher, times(1)).fire(any(OutboxEvent.class));
  }

  @Test
  void salvarOperacaoSaque() {
    Operacao op = Utils.criarOperacaoDefault();
    op.setTipo(TipoTransacao.SAQUE);
    when(contaRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(op.getConta()));
    when(contaService.computeSaldo(any(Conta.class))).thenReturn(BigDecimal.TEN);
    operacaoService.salvarOperacao(op);
    verify(operacaoRepository, times(1)).save(any(Operacao.class));
    verify(eventPublisher, times(0)).fire(any(OutboxEvent.class));
  }

  @Test
  void salvarOperacaoSaqueComSaldoInsuficiente() {
    Operacao op = Utils.criarOperacaoDefault();
    op.setTipo(TipoTransacao.SAQUE);
    when(contaRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(op.getConta()));
    when(contaService.computeSaldo(any(Conta.class))).thenReturn(BigDecimal.ONE);

    assertThrows(BusinessValidationException.class, () -> {
      operacaoService.salvarOperacao(op);
    });
  }

  @Test
  void salvarOperacaoSaqueComOperaçãoInvalida() {
    Operacao op = Utils.criarOperacaoDefault();
    op.setTipo(TipoTransacao.SAQUE);
    op.setTipo(null);
    when(contaRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(op.getConta()));
    when(contaService.computeSaldo(any(Conta.class))).thenReturn(BigDecimal.ONE);

    assertThrows(BusinessValidationException.class, () -> {
      operacaoService.salvarOperacao(op);
    });
  }

  @Test
  void getPageOperacao() {
    operacaoService.getPageOperacao(UUID.randomUUID(), 1, 1);
    verify(operacaoRepository, times(1)).findByContaUuidOrderByDatahoraDesc(any(UUID.class), any(Pageable.class));
  }

  @Test
  void getPageExtrato() {
    LocalDate date = LocalDate.now();
    UUID uuid = UUID.randomUUID();
    operacaoService.getPageExtrato(uuid, date, date.plusDays(3), 1, 1);
    verify(operacaoRepository, times(1)).findByContaUuidAndDatahoraBetweenOrderByDatahoraDesc(any(UUID.class), any(Timestamp.class), any(Timestamp.class),any(Pageable.class));
  }

  @Test
  void getPageExtrato_com_DataInicio_DataFim_nulas() {
    UUID uuid = UUID.randomUUID();
    operacaoService.getPageExtrato(uuid, null, null, 1, 1);
    verify(operacaoRepository, times(1)).findByContaUuidAndDatahoraBetweenOrderByDatahoraDesc(any(UUID.class), any(Timestamp.class), any(Timestamp.class),any(Pageable.class));
  }

  @Test
  void getPageExtrato_DataInicio_maior_DataFim() {
    LocalDate date = LocalDate.now();
    UUID uuid = UUID.randomUUID();
    assertThrows(BusinessValidationException.class, () -> {
      operacaoService.getPageExtrato(uuid, date.plusDays(3), date, 1, 1);
    });
    verify(operacaoRepository, times(0)).findByContaUuidAndDatahoraBetweenOrderByDatahoraDesc(any(UUID.class), any(Timestamp.class), any(Timestamp.class),any(Pageable.class));
  }





}