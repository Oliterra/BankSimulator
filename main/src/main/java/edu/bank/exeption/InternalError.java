package edu.bank.exeption;

public class InternalError extends RuntimeException {

    public InternalError(String message) {
        super(message);
    }
}
