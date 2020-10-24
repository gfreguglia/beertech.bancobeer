package br.com.beertechtalents.lupulo.pocmq.events.template;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class SendMailMessage {
    String email;
    String subject;
    String text;
}
