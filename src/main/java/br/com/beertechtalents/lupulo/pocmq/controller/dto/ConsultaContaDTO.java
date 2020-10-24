package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ConsultaContaDTO {
    final UUID uuid;
    final String nome;
    final Timestamp criadoEm;
    final String email;
    final String cnpj;
}
