package edu.bank.model.entity;

import edu.bank.model.enm.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private String iban;
    private User user;
    private Currency currency;
    private double balance;
    private LocalDate registrationDate;
}
