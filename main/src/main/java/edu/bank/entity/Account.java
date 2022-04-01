package edu.bank.entity;

import edu.bank.enm.Currency;

import java.time.LocalDate;

public class Account {

    private String iban;
    private User user;
    private Currency currency;
    private double balance;
    private LocalDate registrationDate;

    public String getIban() {
        return iban;
    }

    public User getUser() {
        return user;
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getBalance() {
        return balance;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "Account{" +
                "iban='" + iban + '\'' +
                ", user=" + user +
                ", currency=" + currency +
                ", balance=" + balance +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
