package edu.bank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFullInfo {

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
}
