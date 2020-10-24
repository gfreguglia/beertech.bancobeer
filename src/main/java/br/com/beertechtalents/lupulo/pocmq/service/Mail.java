package br.com.beertechtalents.lupulo.pocmq.service;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mail {

    private String corpo;
    private String assunto;
    private String para;

}
