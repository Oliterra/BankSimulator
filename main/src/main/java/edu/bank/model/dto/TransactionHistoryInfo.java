package edu.bank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryInfo {

    private String accountIban;
    private LocalDate fromDate;
    private LocalDate toDate;
}
