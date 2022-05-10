package edu.bank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndividualFullInfoDTO {

    private long id;
    private String name;
    private String lastName;
    private String patronymic;
    private String phone;
    private List<AccountMainInfoDTO> accounts;
}
