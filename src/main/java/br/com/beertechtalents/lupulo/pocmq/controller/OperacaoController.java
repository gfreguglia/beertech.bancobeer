package br.com.beertechtalents.lupulo.pocmq.controller;


import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaOperacaoDTO;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.service.ContaService;
import br.com.beertechtalents.lupulo.pocmq.service.OperacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/operacao")
@Api(value = "Endpoints para transações")
@AllArgsConstructor
@Slf4j
public class OperacaoController {

    OperacaoService operacaoService;
    ContaService contaService;

    @ApiOperation(value = "Adiciona uma nova operacao", nickname = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 405, message = "Invalid input")})
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> novaOperacao(@RequestBody NovaOperacaoDTO dto) {

        // Normalizar entrada
        dto.setValor(dto.getValor().abs());

        Operacao op = new Operacao();
        Optional<Conta> optionalConta = contaService.getConta(dto.getConta());
        if (optionalConta.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        op.setConta(optionalConta.get());
        op.setTipo(dto.getTipo());
        op.setValor(dto.getValor());

        if (op.getTipo().equals(Operacao.TipoTransacao.DEPOSITO)) {
            op.setDescricaoOperacao(Operacao.DescricaoOperacao.DEPOSITO);
        } else {
            op.setDescricaoOperacao(Operacao.DescricaoOperacao.SAQUE);
        }

        operacaoService.salvarOperacao(op);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
