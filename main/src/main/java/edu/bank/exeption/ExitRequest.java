package edu.bank.exeption;

public class ExitRequest extends RuntimeException{

    public ExitRequest() {
        super("Goodbye! Come again!");
    }
}
