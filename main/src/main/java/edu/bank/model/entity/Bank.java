package edu.bank.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bank {

    private long id;
    private String name;
    private String ibanPrefix;
    private double individualsFee;
    private double legalEntitiesFee;
}
