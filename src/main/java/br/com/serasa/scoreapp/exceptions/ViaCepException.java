package br.com.serasa.scoreapp.exceptions;

public class ViaCepException extends Exception {

    public ViaCepException(String message) {
        super(message);
    }

    public ViaCepException(String message, Throwable cause) {
        super(message, cause);
    }
}
