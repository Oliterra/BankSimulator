package edu.bank.model.dto;

import edu.bank.model.entity.LegalEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LegalEntityToUpdate {

    private long id;
    private LegalEntity legalEntity;
}
