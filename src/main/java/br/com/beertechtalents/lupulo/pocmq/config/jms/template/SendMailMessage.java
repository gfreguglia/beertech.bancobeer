package br.com.beertechtalents.lupulo.pocmq.config.jms.template;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendMailMessage {
    String email;
    String subject;
    String text;
}
