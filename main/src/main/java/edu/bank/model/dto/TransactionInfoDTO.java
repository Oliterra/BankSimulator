package edu.bank.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class TransactionInfoDTO {

    private long id;
    private String recipientIban;
    private String senderIban;
    private String recipientBankName;
    private String currency;
    private double sum;
    private double fee;
    private double fullSum;
    private LocalDate date;
    private LocalTime time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRecipientIban() {
        return recipientIban;
    }

    public void setRecipientIban(String recipientIban) {
        this.recipientIban = recipientIban;
    }

    public String getSenderIban() {
        return senderIban;
    }

    public void setSenderIban(String senderIban) {
        this.senderIban = senderIban;
    }

    public String getRecipientBankName() {
        return recipientBankName;
    }

    public void setRecipientBankName(String recipientBankName) {
        this.recipientBankName = recipientBankName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getFullSum() {
        return fullSum;
    }

    public void setFullSum(double fullSum) {
        this.fullSum = fullSum;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Transaction information{" + "\n" +
                "ID:" + id + "\n" +
                "Recipient IBAN: " + recipientIban + "\n" +
                "Sender IBAN: " + senderIban + "\n" +
                "Recipient bank: " + recipientBankName + "\n" +
                "Currency: " + currency + "\n" +
                "Money amount: " + sum + "\n" +
                "Transfer fee: " + fee + "\n" +
                "Full money amount: " + fullSum + "\n" +
                "Date: " + date + "\n" +
                "Time: " + time + "\n";
    }
}
