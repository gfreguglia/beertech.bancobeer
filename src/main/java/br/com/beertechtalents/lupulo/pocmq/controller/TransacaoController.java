package br.com.beertechtalents.lupulo.pocmq.controller;


import br.com.beertechtalents.lupulo.pocmq.model.Transacao;
import br.com.beertechtalents.lupulo.pocmq.service.TransacaoService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transacao")
@Api(value = "Endpoints para transações")
@RequiredArgsConstructor
@Slf4j
public class TransacaoController {

    final TransacaoService transacaoService;

    @ApiOperation(value = "Busca saldo total", nickname = "GET", response = BigDecimal.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = BigDecimal.class),
            @ApiResponse(code = 400, message = "Invalid status value")})
    @GetMapping
    public ResponseEntity<BigDecimal> getSaldo() {
        return new ResponseEntity<>(transacaoService.buscarSaldo(), HttpStatus.OK);
    }

    @ApiOperation(value = "Adiciona uma nova transacao", nickname = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 405, message = "Invalid input")})
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void novaOperacao(@RequestBody Transacao body) {

        // Normalizar entrada
        if(body.getTipo().equals(Transacao.TipoTransacao.SAQUE)) {
            body.setValor(body.getValor().abs().negate());
        } else {
            body.setValor(body.getValor().abs());
        }
        transacaoService.salvarTransacao(body);
    }
}
