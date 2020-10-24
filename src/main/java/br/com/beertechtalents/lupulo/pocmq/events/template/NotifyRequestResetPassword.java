package br.com.beertechtalents.lupulo.pocmq.events.template;

import br.com.beertechtalents.lupulo.pocmq.model.TokenTrocarSenha;

public class NotifyRequestResetPassword extends SendMailMessage {

    private static final String REQUEST_SUBJECT = "Pedido de alteação de senha";
    private static final String REQUEST_TEXT = "Você fez um pedido de alteração, acesse o link %s%s";

    public NotifyRequestResetPassword(String email, String subject, String text) {
        super(email, subject, text);
    }

    public NotifyRequestResetPassword(TokenTrocarSenha token, String path) {
        this(token.getConta().getEmail(), REQUEST_SUBJECT, String.format(REQUEST_TEXT, path, token.getUuid().toString()));
    }
}
