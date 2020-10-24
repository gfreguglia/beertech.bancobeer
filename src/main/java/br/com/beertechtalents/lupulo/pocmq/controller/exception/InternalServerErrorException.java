package br.com.beertechtalents.lupulo.pocmq.controller.exception;

import java.util.List;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String... details) {
        super(InternalServerErrorException.generateMessage(details));
    }

    public InternalServerErrorException(List<String> details) {
        super(InternalServerErrorException.generateMessage(details));
    }

    private static String generateMessage(String... searchParams) {
        return String.join(".\n", searchParams);
    }

    private static String generateMessage(List<String> searchParams) {
        return String.join(".\n", searchParams);
    }
}
