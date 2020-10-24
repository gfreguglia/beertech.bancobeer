package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import br.com.beertechtalents.lupulo.pocmq.validators.NullOrNotEmpty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Optional;

@Data
public class AtualizarContaDTO {
    private Optional<@NullOrNotEmpty String> password = Optional.empty();
    private Optional<@NullOrNotEmpty String> nome = Optional.empty();
    private Optional<@Size(min = 14, max = 14) String> cnpj = Optional.empty();
}
