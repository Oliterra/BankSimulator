package edu.bank.model.dto;

import edu.bank.model.enm.Currency;

public class AccountMainInfoDTO {

    private String iban;
    private Currency currency;
    private double balance;
    private String bankName;

    public AccountMainInfoDTO(String iban, Currency currency, double balance, String bankName) {
        this.iban = iban;
        this.currency = currency;
        this.balance = balance;
        this.bankName = bankName;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String toString() {
        return "Account info: " +
                "Bank: " + bankName + ", " +
                "Currency: " + currency + ", " +
                "Balance: " + balance + ", " +
                "IBAN: " + iban;
    }
}
