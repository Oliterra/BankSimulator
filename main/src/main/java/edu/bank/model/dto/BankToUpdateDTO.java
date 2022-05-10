package edu.bank.model.dto;

import edu.bank.model.entity.Bank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankToUpdateDTO {

    private long id;
    private Bank bank;
}
