package edu.bank.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Individual extends User {

    private String lastName;
    private String patronymic;
}
