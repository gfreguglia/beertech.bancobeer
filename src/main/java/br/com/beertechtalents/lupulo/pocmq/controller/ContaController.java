package br.com.beertechtalents.lupulo.pocmq.controller;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.ConsultaContaDTO;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.ConsultaExtratoDTO;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.ConsultaSaldoDTO;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaContaDTO;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.PatchTrocarSenhaDTO;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.service.ContaService;
import br.com.beertechtalents.lupulo.pocmq.service.OperacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.sql.Timestamp;
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

    OperacaoService operacaoService;

    @ApiOperation("Consulta paginada de contas")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<ConsultaContaDTO>> getContas(@ApiParam("Indice da pagina requisitada") @RequestParam(defaultValue = "0", required = false) @Min(0) int page,
                                                            @ApiParam("Numero de elementos por pagina") @RequestParam(defaultValue = "25", required = false) @Min(10) @Max(50) int size) {
        Page<ConsultaContaDTO> map = contaService.getPageConta(page, size)
                .map(conta -> new ConsultaContaDTO(conta.getUuid(), conta.getNome(), conta.getCriadoEm(), conta.getEmail(), conta.getCnpj()));

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
        ConsultaContaDTO dto = new ConsultaContaDTO(conta.getUuid(), conta.getNome(), conta.getCriadoEm(), conta.getEmail(), conta.getCnpj());
        return ResponseEntity.ok(dto);
    }

    @ApiOperation("Consulta de conta por email")
    @GetMapping(params = "email",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConsultaContaDTO> getContaPorEmail(
            @ApiParam("Email da conta") @RequestParam(required = true) @Email String email) {

        Optional<Conta> optionalConta= contaService.findByEmail(email);

        if (optionalConta.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Conta conta = optionalConta.get();
        ConsultaContaDTO dto = new ConsultaContaDTO(conta.getUuid(), conta.getNome(), conta.getCriadoEm(), conta.getEmail(), conta.getCnpj());
        return ResponseEntity.ok(dto);
    }

    @ApiOperation("Consulta de conta por cnpj")
    @GetMapping(params = "cnpj",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConsultaContaDTO> getContaPorCnpj(
            @ApiParam("Cnpj da conta") @RequestParam(required = false) String cnpj) {

        Optional<Conta> optionalConta = contaService.findByCnpj(cnpj);

        if (optionalConta.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Conta conta = optionalConta.get();
        ConsultaContaDTO dto = new ConsultaContaDTO(conta.getUuid(), conta.getNome(), conta.getCriadoEm(), conta.getEmail(), conta.getCnpj());
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAuthority(#uuid.toString())")
    @ApiOperation(value = "Consulta saldo", response = ConsultaSaldoDTO.class)
    @GetMapping(value = "/{uuid}/saldo", produces = {MediaType.APPLICATION_JSON_VALUE})
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
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UUID> postConta(@RequestBody NovaContaDTO dto) {
        Conta conta = new Conta();
        conta.setNome(dto.getNome());
        conta.setEmail(dto.getEmail());
        conta.setCnpj(dto.getCnpj());
        conta.setSenha(dto.getSenha());
        conta = contaService.novaConta(conta);
        return ResponseEntity.ok(conta.getUuid());
    }

    @PreAuthorize("hasAuthority(#uuid.toString())")
    @ApiOperation("Consulta paginada dos ultimos lancamentos")
    @GetMapping(value = "/{uuid}/operacao", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<ConsultaExtratoDTO>> getOperacoes(
            @PathVariable UUID uuid,
            @ApiParam("Indice da pagina requisitada") @RequestParam(defaultValue = "0", required = false) @Min(0) int page,
            @ApiParam("Numero de elementos por pagina") @RequestParam(defaultValue = "25", required = false) @Min(10) @Max(50) int size) {
        Page<ConsultaExtratoDTO> map = operacaoService.getPageOperacao(uuid, page, size)
                .map(operacao -> new ConsultaExtratoDTO(operacao.getId(), operacao.getTipo(),
                        operacao.getDescricaoOperacao(), operacao.getValor()));

        return ResponseEntity.ok(map);
    }

    @PreAuthorize("hasAuthority(#uuid.toString())")
    @ApiOperation("Consulta paginada dos extrato")
    @GetMapping(value = "/{uuid}/extrato", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<ConsultaExtratoDTO>> getContas(
            @PathVariable UUID uuid,
            @ApiParam("Data inicial do extrato") @RequestParam Timestamp inicio,
            @ApiParam("Data final do extrato") @RequestParam Timestamp fim,
            @ApiParam("Indice da pagina requisitada") @RequestParam(defaultValue = "0", required = false) @Min(0) int page,
            @ApiParam("Numero de elementos por pagina") @RequestParam(defaultValue = "25", required = false) @Min(10) @Max(50) int size

    ) {
        Page<ConsultaExtratoDTO> map = operacaoService.getPageExtrato(uuid, inicio, fim, page, size)
                .map(operacao -> new ConsultaExtratoDTO(operacao.getId(), operacao.getTipo(),
                        operacao.getDescricaoOperacao(), operacao.getValor()));

        return ResponseEntity.ok(map);
    }

    @ApiOperation("Envia um email link para de redifinir senha")
    @PostMapping("/pedido-resetar-senha")
    public ResponseEntity<?> requestTrocarSenha(@ApiParam("Email da conta") @RequestBody @Email String email) {

        contaService.sendRequestTrocarSenha(email);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Pega a conta do token de resetar senha")
    @PostMapping("/conta-by-token-restar-senha")
    public ResponseEntity<?>getContabyTokenResetarSenha(@ApiParam("Token de reset de senha") @RequestBody UUID tokenUuid) {

        Optional<Conta> optionalConta = contaService.getContabyTokenTrocarSenha(tokenUuid);

        if (optionalConta.isPresent()) {
            Conta conta = optionalConta.get();

            ConsultaContaDTO dto = new ConsultaContaDTO(conta.getUuid(), conta.getNome(), conta.getCriadoEm(), conta.getEmail(), conta.getCnpj());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation("Troca a senha da conta")
    @PatchMapping("/{uuid}")
    public ResponseEntity<?>trocarSenha(@PathVariable UUID uuid,
                                        @ApiParam("Nova senha e token de resetar senha") @RequestBody PatchTrocarSenhaDTO trocarSenhaDTO) {

        contaService.trocarSenha(uuid, trocarSenhaDTO.getSenha(), trocarSenhaDTO.getToken());
        return ResponseEntity.ok().build();
    }
}
