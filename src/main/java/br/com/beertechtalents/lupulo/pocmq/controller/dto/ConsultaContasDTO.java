package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@Value
public class ConsultaContaDTO {
    UUID uuid;
    String nome;
    Timestamp criadoEm;
}
