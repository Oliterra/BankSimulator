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

    public String getRecipientIban() {
        return recipientIban;
    }

    public String getSenderIban() {
        return senderIban;
    }

    public String getRecipientBankName() {
        return recipientBankName;
    }

    public String getCurrency() {
        return currency;
    }

    public double getSum() {
        return sum;
    }

    public double getFee() {
        return fee;
    }

    public double getFullSum() {
        return fullSum;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRecipientIban(String recipientIban) {
        this.recipientIban = recipientIban;
    }

    public void setSenderIban(String senderIban) {
        this.senderIban = senderIban;
    }

    public void setRecipientBankName(String recipientBankName) {
        this.recipientBankName = recipientBankName;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public void setFullSum(double fullSum) {
        this.fullSum = fullSum;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Transaction information{" + "\n" +
                "id:" + id + "\n" +
                "recipient IBAN: " + recipientIban + "\n" +
                "sender IBAN: " + senderIban + "\n" +
                "recipient bank: " + recipientBankName + "\n" +
                "currency: " + currency + "\n" +
                "money amount: " + sum + "\n" +
                "transfer fee: " + fee + "\n" +
                "full money amount: " + fullSum + "\n" +
                "date: " + date + "\n" +
                "time: " + time + "\n";
    }
}
