package br.com.beertechtalents.lupulo.pocmq.controller;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.ConsultaContaDTO;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.ConsultaSaldoDTO;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.service.ContaService;
import io.swagger.annotations.*;
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
@Api("Endpoints para gerenciamento de contas")
public class ContaController {

    ContaService contaService;

    @ApiOperation("Consulta paginada de contas")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<ConsultaContaDTO>> getContas(@ApiParam("Indice da pagina requisitada") @RequestParam(defaultValue = "0", required = false) @Min(0) int page,
                                                            @ApiParam("Numero de elementos por pagina") @RequestParam(defaultValue = "25", required = false) @Min(10) @Max(50) int size) {
        Page<ConsultaContaDTO> map = contaService.getPageConta(page, size)
                .map(conta -> new ConsultaContaDTO(conta.getUuid(), conta.getNome(), conta.getCriadoEm()));

        return ResponseEntity.ok(map);
    }

    @ApiOperation("Consulta de conta")
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

    @ApiOperation(value = "Consulta saldo", response = ConsultaSaldoDTO.class)
    @GetMapping(value = "/{uuid}/saldo", produces = {MediaType.TEXT_PLAIN_VALUE})
    public CompletableFuture<ResponseEntity<ConsultaSaldoDTO>> getSaldoConta(@PathVariable UUID uuid) {
        Optional<Conta> conta = contaService.getConta(uuid);
        if (conta.isEmpty()) {
            return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return CompletableFuture.supplyAsync(() -> {
            BigDecimal bigDecimal = this.contaService.computeSaldo(conta.get());
            ConsultaSaldoDTO dto = new ConsultaSaldoDTO();
            dto.setSaldo(bigDecimal);
            dto.setUuid(conta.get().getUuid());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        });
    }

    @ApiOperation("Criar nova conta")
    @PostMapping(consumes = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<UUID> postConta(@RequestBody String nome) {
        Conta conta = new Conta();
        conta.setNome(nome);
        conta = contaService.novaConta(conta);
        return ResponseEntity.ok(conta.getUuid());
    }
}
