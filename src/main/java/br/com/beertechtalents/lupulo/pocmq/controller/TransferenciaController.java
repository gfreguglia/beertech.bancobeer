package br.com.beertechtalents.lupulo.pocmq.controller;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaTransferenciaDTO;
import br.com.beertechtalents.lupulo.pocmq.service.TransferenciaService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/transferencia")
@Validated
@AllArgsConstructor
public class TransferenciaController {

    TransferenciaService transferenciaService;

    @PostMapping
    @ApiOperation(value = "Realizar transferencia")
    public CompletableFuture<?> novaTransferencia(@RequestBody NovaTransferenciaDTO dto) {


        return CompletableFuture.supplyAsync(() -> {
            try {
                transferenciaService.transferir(dto.getOrigem(), dto.getDestino(), dto.getValor());
            } catch (HttpClientErrorException ex) {
                return new ResponseEntity<>(ex.getStatusCode());
            }

            return ResponseEntity.noContent().build();
        });

    }
}
