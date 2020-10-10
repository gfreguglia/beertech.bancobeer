package br.com.beertechtalents.lupulo.pocmq.controller;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.ConsultaContasDTO;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Transacao;
import br.com.beertechtalents.lupulo.pocmq.service.ContaService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Stream;

@RestController
@RequestMapping("/conta")
@AllArgsConstructor
@Validated
public class ContaController {

    ContaService contaService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<ConsultaContasDTO>> getContas(@RequestParam(defaultValue = "0", required = false) @Min(0) int page,
                                                             @RequestParam(defaultValue = "10", required = false) @Min(10) @Max(50) int size) {
        Page<ConsultaContasDTO> map = contaService.getPageConta(page, size)
                .map(conta -> new ConsultaContasDTO(conta.getUuid(), conta.getNome(), conta.getCriadoEm()));

        return ResponseEntity.ok(map);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Conta> getConta(@PathVariable UUID uuid) {
        Optional<Conta> optionalConta = contaService.getConta(uuid);
        if (optionalConta.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(optionalConta.get());
    }

    @GetMapping("/{uuid}/saldo")
    public DeferredResult<ResponseEntity<BigDecimal>> getSaldoConta(@PathVariable UUID uuid) throws ExecutionException, InterruptedException {

        DeferredResult<ResponseEntity<BigDecimal>> output = new DeferredResult<>();

        Optional<Conta> optionalConta = contaService.getConta(uuid);
        if (optionalConta.isEmpty()) {
            output.setResult(new ResponseEntity<>(HttpStatus.NOT_FOUND));
            return output;
        }

        ForkJoinPool.commonPool().submit(() -> {
            Conta conta = optionalConta.get();
            BigDecimal reduce = Stream.of(conta.getEntradas(), conta.getSaidas())
                    .flatMap(Collection::stream)
                    .map(Transacao::getValor)
                    .reduce(new BigDecimal(0.0), BigDecimal::add);

            output.setResult(ResponseEntity.ok(reduce));
        });

        return output;
    }

    @PostMapping(consumes = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<UUID> postConta(@RequestBody String nome) {
        Conta conta = new Conta();
        conta.setNome(nome);
        conta = contaService.novaConta(conta);
        return ResponseEntity.ok(conta.getUuid());
    }
}
