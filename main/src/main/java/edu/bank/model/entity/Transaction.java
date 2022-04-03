package edu.bank.model.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

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

    public void setId(long id) {
        this.id = id;
    }

    public Account getRecipientAccount() {
        return recipientAccount;
    }

    public void setRecipientAccount(Account recipientAccount) {
        this.recipientAccount = recipientAccount;
    }

    public Account getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(Account senderAccount) {
        this.senderAccount = senderAccount;
    }

    public double getFullSum() {
        return fullSum;
    }

    public void setFullSum(double fullSum) {
        this.fullSum = fullSum;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", recipientAccount=" + recipientAccount +
                ", senderAccount=" + senderAccount +
                ", fullSum=" + fullSum +
                ", fee=" + fee +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
