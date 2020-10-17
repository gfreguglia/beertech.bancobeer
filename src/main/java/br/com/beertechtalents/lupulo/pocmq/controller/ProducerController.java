package br.com.beertechtalents.lupulo.pocmq.controller;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaOperacaoDTO;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaTransferenciaDTO;
import br.com.beertechtalents.lupulo.pocmq.service.ProducerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @ApiOperation("Criar nova operação")
    @PostMapping(value = "/operacao", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> postOperacao(@RequestBody NovaOperacaoDTO operacaoDTO) {
        producerService.sendOperacao(operacaoDTO);

        return ResponseEntity.accepted().build();
    }

    @ApiOperation("Criar nova transferencia")
    @PostMapping(value = "/transferencia", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> postTransferencia(@RequestBody NovaTransferenciaDTO transferenciaDTO) {
        producerService.sendTransferencia(transferenciaDTO);

        return ResponseEntity.accepted().build();
    }
}
