package br.com.beertechtalents.lupulo.pocmq.controller;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.ConsultaContaDTO;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.service.ContaService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/conta")
@AllArgsConstructor
@Validated
public class ContaController {

    ContaService contaService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<ConsultaContaDTO>> getContas(@RequestParam(defaultValue = "0", required = false) @Min(0) int page,
                                                            @RequestParam(defaultValue = "10", required = false) @Min(10) @Max(50) int size) {
        Page<ConsultaContaDTO> map = contaService.getPageConta(page, size)
                .map(conta -> new ConsultaContaDTO(conta.getUuid(), conta.getNome(), conta.getCriadoEm()));

        return ResponseEntity.ok(map);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ConsultaContaDTO> getConta(@PathVariable UUID uuid) {
        Optional<Conta> optionalConta = contaService.getConta(uuid);
        if (optionalConta.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Conta conta = optionalConta.get();
        ConsultaContaDTO dto = new ConsultaContaDTO(conta.getUuid(), conta.getNome(), conta.getCriadoEm());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{uuid}/saldo")
    public CompletableFuture<ResponseEntity<BigDecimal>> getSaldoConta(@PathVariable UUID uuid) {
        Optional<Conta> conta = contaService.getConta(uuid);
        if (conta.isEmpty()) {
            return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(this.contaService.computeSaldo(conta.get())));
    }

    @PostMapping(consumes = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<UUID> postConta(@RequestBody String nome) {
        Conta conta = new Conta();
        conta.setNome(nome);
        conta = contaService.novaConta(conta);
        return ResponseEntity.ok(conta.getUuid());
    }
}
