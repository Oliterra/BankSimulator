package edu.bank.dto;

import edu.bank.enm.Currency;

public class AccountMainInfoDTO {

    private String iban;
    private Currency currency;
    private double amount;
    private String bankName;

    public AccountMainInfoDTO(String iban, Currency currency, double amount, String bankName) {
        this.iban = iban;
        this.currency = currency;
        this.amount = amount;
        this.bankName = bankName;
    }

    public String getIban() {
        return iban;
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Account info: " +
                "bank=" + bankName +
                ", currency=" + currency +
                ", amount=" + amount +
                ", iban=" + iban;
    }
}
