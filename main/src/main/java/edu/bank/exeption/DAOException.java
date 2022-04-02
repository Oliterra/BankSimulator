package edu.bank.exeption;

public class DAOException extends RuntimeException{

    public DAOException() {
        super("Sorry, an internal error has occurred. Try again later");
    }
}
