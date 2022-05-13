package edu.bank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankFullInfo {
    private long id;
    private String name;
    private String ibanPrefix;
    private double individualsFee;
    private double legalEntitiesFee;
    private int usersCount;
}