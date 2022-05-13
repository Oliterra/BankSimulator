package edu.bank.model.dto;

import edu.bank.model.enm.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountMainInfo {

    private String iban;
    private Currency currency;
    private double balance;
    private String bankName;
}
