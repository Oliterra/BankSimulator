package edu.bank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LegalEntityFullInfo {

    private long id;
    private String name;
    private String phone;
    private List<AccountMainInfo> accounts;
}
