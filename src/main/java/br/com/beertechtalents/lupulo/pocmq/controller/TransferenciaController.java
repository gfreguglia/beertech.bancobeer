package br.com.beertechtalents.lupulo.pocmq.controller;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaTransferenciaDTO;
import br.com.beertechtalents.lupulo.pocmq.controller.exception.BusinessValidationException;
import br.com.beertechtalents.lupulo.pocmq.service.TransferenciaService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/transferencia")
@Validated
@AllArgsConstructor
public class TransferenciaController {

    TransferenciaService transferenciaService;

    @Async
    @PostMapping
    @ApiOperation(value = "Realizar transferencia")
    @PreAuthorize("hasAuthority(#dto.origem)")
    public ResponseEntity<Void> novaTransferencia(@Valid @RequestBody NovaTransferenciaDTO dto) {
        if(dto.getDestino().equals(dto.getOrigem())) {
            throw new BusinessValidationException("Cannot transfer to yourself");
        }
        transferenciaService.transferir(dto.getOrigem(), dto.getDestino(), dto.getValor());
        return ResponseEntity.noContent().build();

    }
}
