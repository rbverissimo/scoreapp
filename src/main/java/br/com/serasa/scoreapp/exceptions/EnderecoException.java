package br.com.serasa.scoreapp.exceptions;

public class EnderecoException extends Exception {

    public EnderecoException(String message) {
        super(message);
    }

    public EnderecoException(String message, Throwable cause) {
        super(message, cause);
    }
}
