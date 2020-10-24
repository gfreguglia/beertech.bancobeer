package br.com.beertechtalents.lupulo.pocmq.controller;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaOperacaoJms;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaTransferenciaDTO;
import br.com.beertechtalents.lupulo.pocmq.service.ProducerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producer")
@AllArgsConstructor
@Validated
@Api("Endpoints para envio de requisições")
public class ProducerController {

    ProducerService producerService;

    @PreAuthorize("hasAuthority(#novaOperacaoJms.conta.toString())")
    @ApiOperation("Novo Saque")
    @PostMapping(value = "/operacao:saque", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> postOperacao(@RequestBody NovaOperacaoJms novaOperacaoJms) {
        producerService.sendSaque(novaOperacaoJms);

        return ResponseEntity.accepted().build();
    }

    @PreAuthorize("hasAuthority(#novaOperacaoJms.conta.toString())")
    @ApiOperation("Novo Deposito")
    @PostMapping(value = "/operacao:deposito", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> postDeposito(@RequestBody NovaOperacaoJms novaOperacaoJms) {
        producerService.sendDeposito(novaOperacaoJms);

        return ResponseEntity.accepted().build();
    }

    @PreAuthorize("hasAuthority(#transferenciaDTO.origem.toString())")
    @ApiOperation("Criar nova transferencia")
    @PostMapping(value = "/transferencia", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> postTransferencia(@RequestBody NovaTransferenciaDTO transferenciaDTO) {
        producerService.sendTransferencia(transferenciaDTO);

        return ResponseEntity.accepted().build();
    }
}
