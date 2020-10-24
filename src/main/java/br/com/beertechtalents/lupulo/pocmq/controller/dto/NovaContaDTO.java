package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
public class NovaContaDTO {
    @NotBlank
    String nome;

    @NotBlank
    String email;

    @NotBlank
    String cnpj;

    @NotBlank
    String senha;
}
