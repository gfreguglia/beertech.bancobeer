package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ConsultaContasDTO {
    private UUID uuid;
    private String nome;
    private Timestamp criadoEm;
}
