package edu.bank.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private long id;
    private Account recipientAccount;
    private Account senderAccount;
    private double fullSum;
    private double fee;
    private LocalDate date;
    private LocalTime time;
}
