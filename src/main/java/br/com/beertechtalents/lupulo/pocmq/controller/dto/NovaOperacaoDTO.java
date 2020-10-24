package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.validator.ValueOfEnum;
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

    @ValueOfEnum(enumClass = Operacao.TipoTransacao.class, message = "Tipo de operação não permitida")
    @ApiModelProperty(notes = "Valid values:\n DEPOSITO \nSAQUE")
    private String tipo;

    private BigDecimal valor;

    @ValueOfEnum(enumClass = Operacao.Categoria.class, message = "Categoria não permitida")
    @ApiModelProperty(notes = "Valid values:\n ALIMENTACAO \nSAUDE \nTRANSPORTE \nENTRETENIMENTO ")
    private String categoria;

}
