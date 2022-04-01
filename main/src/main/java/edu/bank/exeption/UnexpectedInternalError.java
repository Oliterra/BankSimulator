package edu.bank.exeption;

public class UnexpectedInternalError extends RuntimeException{

    public UnexpectedInternalError() {
        super("Sorry, an internal error has occurred. Try again later");
    }
}
