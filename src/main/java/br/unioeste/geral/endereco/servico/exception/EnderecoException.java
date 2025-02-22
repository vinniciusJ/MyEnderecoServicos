package br.unioeste.geral.endereco.servico.exception;

import java.io.Serial;

public class EnderecoException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public EnderecoException(String errorMessage) {
        super(errorMessage);
    }
}
