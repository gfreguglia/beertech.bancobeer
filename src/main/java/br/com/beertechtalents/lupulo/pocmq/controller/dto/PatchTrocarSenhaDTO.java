package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PatchTrocarSenhaDTO {

    String senha;
    UUID token;
}
