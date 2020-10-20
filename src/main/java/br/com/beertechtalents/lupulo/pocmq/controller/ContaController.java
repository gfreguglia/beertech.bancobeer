package br.com.beertechtalents.lupulo.pocmq.controller;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.ConsultaContaDTO;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaContaDTO;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.service.ContaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/conta")
@AllArgsConstructor
@Validated
@Api("Endpoints para gerenciamento de contas")
public class ContaController {

    ContaService contaService;

    @ApiOperation("Consulta paginada de contas")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<ConsultaContaDTO>> getContas(@ApiParam("Indice da pagina requisitada")
                                                            @RequestParam(defaultValue = "0", required = false)
                                                            @Min(0) int page,
                                                            @ApiParam("Numero de elementos por pagina")
                                                            @RequestParam(defaultValue = "25", required = false)
                                                            @Min(10) @Max(50) int size) {
        Page<ConsultaContaDTO> map = contaService.getPageConta(page, size)
                .map(conta -> new ConsultaContaDTO(conta.getUuid(), conta.getNome(), conta.getCriadoEm(),
                        conta.getSaldo(), conta.getPerfil(), conta.getEmail(), conta.getCnpj()));

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
        ConsultaContaDTO dto = new ConsultaContaDTO(conta.getUuid(), conta.getNome(), conta.getCriadoEm(),
                conta.getSaldo(), conta.getPerfil(), conta.getEmail(), conta.getCnpj());
        return ResponseEntity.ok(dto);
    }

    @ApiOperation("Criar nova conta")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UUID> postConta(@RequestBody NovaContaDTO dto) {
        Conta conta = new Conta();
        conta.setNome(dto.getNome());
        conta.setEmail(dto.getEmail());
        conta.setCnpj(dto.getCnpj());
        conta.setSenha(dto.getSenha());
        conta.setPerfil(dto.getPerfil());
        conta = contaService.novaConta(conta);
        return ResponseEntity.ok(conta.getUuid());
    }
}
