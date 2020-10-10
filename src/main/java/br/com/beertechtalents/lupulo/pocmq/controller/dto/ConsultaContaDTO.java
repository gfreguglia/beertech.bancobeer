package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
public class ConsultaContaDTO extends ConsultaContasDTO {
    public ConsultaContaDTO(UUID uuid, String nome, Timestamp criadoEm, BigDecimal saldo) {
        super(uuid, nome, criadoEm);
        this.saldo = saldo;
    }

    BigDecimal saldo;

}
