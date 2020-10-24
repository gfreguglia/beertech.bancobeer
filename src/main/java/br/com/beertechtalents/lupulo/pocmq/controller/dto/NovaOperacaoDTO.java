package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.validator.CategoryPolicy;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class NovaOperacaoDTO {
    private UUID conta;
    private Operacao.TipoTransacao tipo;
    private BigDecimal valor;

    @CategoryPolicy(message = "Categoria não permitida")
    @ApiModelProperty(notes = "Valid values:\n ALIMENTACAO \nSAUDE \nTRANSPORTE \nENTRETENIMENTO ")
    private String categoria;


}
