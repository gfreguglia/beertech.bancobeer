package br.com.beertechtalents.lupulo.pocmq.controller;


import br.com.beertechtalents.lupulo.pocmq.model.Transacao;
import br.com.beertechtalents.lupulo.pocmq.service.TransacaoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transacao")
@RequiredArgsConstructor
public class TrasacaoController {

    final TransacaoService transacaoService;

    @ApiOperation(value = "Busca saldo total", nickname = "GET", notes = "Busca o saldo total", response = BigDecimal.class, tags={ "tool", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = BigDecimal.class),
            @ApiResponse(code = 400, message = "Invalid status value") })
    @RequestMapping(
            produces = { "application/json" },
            method = RequestMethod.GET)
    public ResponseEntity<BigDecimal> getSaldo(){
        return new ResponseEntity<>(transacaoService.buscarSaldo(), HttpStatus.OK);
    }

    @ApiOperation(value = "Adiciona uma nova transacao", nickname = "POST", notes = "", tags={ "transacao", })
    @ApiResponses(value = {
            @ApiResponse(code = 405, message = "Invalid input") })
    @RequestMapping(value = "",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    public void novaOperacao(@ApiParam(value = "Tool object that needs to be added" ,required=true )  @RequestBody Transacao body){
        transacaoService.salvarTransacao(body);
    }
}
