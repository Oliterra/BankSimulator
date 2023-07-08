package edu.bank.model.dto;

import edu.bank.model.entity.Individual;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndividualToCreate {

    private long bankId;
    private Individual individual;
}
