package br.com.beertechtalents.lupulo.pocmq.events.template;

import br.com.beertechtalents.lupulo.pocmq.model.Operacao;

public class NotifyDeposit extends SendMailMessage {
    private static final String DEPOSIT_SUBJECT = "Dinheiro na conta, você tem. Hmm...";
    private static final String DEPOSIT_TEXT = "Você recebeu %.2f";

    public NotifyDeposit(String email, String subject, String text) {
        super(email, subject, text);
    }

    public NotifyDeposit(Operacao op) {
        this(op.getConta().getEmail(), DEPOSIT_SUBJECT, String.format(DEPOSIT_TEXT, op.getValor()));
    }
}
