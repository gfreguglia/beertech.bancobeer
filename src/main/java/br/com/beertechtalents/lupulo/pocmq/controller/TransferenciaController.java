package br.com.beertechtalents.lupulo.pocmq.controller;

import br.com.beertechtalents.lupulo.pocmq.service.TransferenciaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/transferencia")
@Validated
@AllArgsConstructor
public class TransferenciaController {

    TransferenciaService transferenciaService;

    @PostMapping
    public ResponseEntity<?> novaTransferencia(@RequestParam UUID origem,
                                               @RequestParam UUID destino,
                                               @RequestParam @Min(0) BigDecimal valor) {

        try {
            transferenciaService.transferir(origem, destino, valor);
        } catch (HttpClientErrorException ex) {
            return new ResponseEntity<>(ex.getLocalizedMessage(), ex.getStatusCode());
        }

        return ResponseEntity.noContent().build();
    }
}
