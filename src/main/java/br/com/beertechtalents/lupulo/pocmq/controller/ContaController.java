package br.com.beertechtalents.lupulo.pocmq.controller;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.*;
import br.com.beertechtalents.lupulo.pocmq.controller.exception.EntityNotFoundException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    PasswordEncoder passwordEncoder;

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
        return getConsultaContaDTOResponseEntity(optionalConta);
    }

    @ApiOperation("Atualizar dados da conta")
    @PatchMapping("/{uuid}")
    public ResponseEntity<Void> patchConta(@PathVariable UUID uuid, @Valid @RequestBody AtualizarContaDTO dto) {

        Conta conta = contaService.getConta(uuid).orElseThrow(() -> new EntityNotFoundException(Conta.class, "UUID: %s", uuid));

        dto.getCnpj().ifPresent(conta::setCnpj);
        dto.getNome().ifPresent(conta::setNome);
        dto.getPassword().ifPresent(s -> conta.setSenha(passwordEncoder.encode(s)));

        contaService.salvar(conta);

        return ResponseEntity.noContent().build();

    }

    @ApiOperation("Consulta de conta por email")
    @GetMapping(params = "email", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConsultaContaDTO> getContaPorEmail(
            @ApiParam("Email da conta") @RequestParam(required = true) @Email String email) {

        Optional<Conta> optionalConta = contaService.findByEmail(email);

        return getConsultaContaDTOResponseEntity(optionalConta);
    }

    private ResponseEntity<ConsultaContaDTO> getConsultaContaDTOResponseEntity(Optional<Conta> optionalConta) {
        if (optionalConta.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Conta conta = optionalConta.get();
        ConsultaContaDTO dto = new ConsultaContaDTO(conta.getUuid(), conta.getNome(), conta.getCriadoEm(), conta.getEmail(), conta.getCnpj());
        return ResponseEntity.ok(dto);
    }

    @ApiOperation("Consulta de conta por cnpj")
    @GetMapping(params = "cnpj", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConsultaContaDTO> getContaPorCnpj(
            @ApiParam("Cnpj da conta") @RequestParam(required = false) String cnpj) {

        Optional<Conta> optionalConta = contaService.findByCnpj(cnpj);

        return getConsultaContaDTOResponseEntity(optionalConta);
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
    public ResponseEntity<UUID> postConta(@Valid @RequestBody NovaContaDTO dto) {
        Conta conta = new Conta();
        conta.setNome(dto.getNome());
        conta.setEmail(dto.getEmail());
        conta.setCnpj(dto.getCnpj());
        conta.setSenha(passwordEncoder.encode(dto.getSenha()));
        conta = contaService.salvar(conta);
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
                .map(operacao -> new ConsultaExtratoDTO(operacao.getId(),
                        operacao.getTipo(),
                        operacao.getDescricaoOperacao(),
                        operacao.getValor(),
                        operacao.getCategoria() != null ? operacao.getCategoria().toString() : null,
                        operacao.getDatahora(),
                        operacao.getSaldoAtual()));

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
                .map(operacao -> new ConsultaExtratoDTO(operacao.getId(),
                        operacao.getTipo(),
                        operacao.getDescricaoOperacao(),
                        operacao.getValor(),
                        operacao.getCategoria() != null ? operacao.getCategoria().toString() : null,
                        operacao.getDatahora(),
                        operacao.getSaldoAtual()));

        return ResponseEntity.ok(map);
    }

    @ApiOperation("Envia um email link para de redifinir senha")
    @PostMapping("/pedido-resetar-senha")
    public ResponseEntity<Void> requestTrocarSenha(@ApiParam("Email da conta") @RequestBody @Email String email) {

        contaService.sendRequestTrocarSenha(email);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation("Pega a conta do token de resetar senha")
    @PostMapping("/conta-by-token-restar-senha")
    public ResponseEntity<ConsultaContaDTO> getContabyTokenResetarSenha(@ApiParam("Token de reset de senha") @RequestBody UUID tokenUuid) {

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
    @PostMapping("/{uuid}/trocar-senha")
    public ResponseEntity<Void> trocarSenha(@PathVariable UUID uuid,
                                            @ApiParam("Nova senha e token de resetar senha") @RequestBody PatchTrocarSenhaDTO trocarSenhaDTO) {

        contaService.trocarSenha(uuid, trocarSenhaDTO.getSenha(), trocarSenhaDTO.getToken());
        return ResponseEntity.noContent().build();
    }
}
