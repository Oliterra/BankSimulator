package edu.bank.model.dto;

import edu.bank.model.command.CommandParamInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandFullInfo {

    private String name;
    private String description;
    private Set<CommandParamInfo> params;
}
