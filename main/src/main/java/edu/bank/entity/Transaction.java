package edu.bank.entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {

    private long id;
    private Account recipientAccount;
    private Account senderAccount;
    private double fullSum;
    private double fee;
    private LocalDate date;
    private LocalTime time;

    public long getId() {
        return id;
    }

    public Account getRecipientAccount() {
        return recipientAccount;
    }

    public Account getSenderAccount() {
        return senderAccount;
    }

    public double getFullSum() {
        return fullSum;
    }

    public double getFee() {
        return fee;
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

    public void setRecipientAccount(Account recipientAccount) {
        this.recipientAccount = recipientAccount;
    }

    public void setSenderAccount(Account senderAccount) {
        this.senderAccount = senderAccount;
    }

    public void setFullSum(double fullSum) {
        this.fullSum = fullSum;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
