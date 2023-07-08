package edu.bank.command.info;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@Component
public class CommandInfo {

    private String name;
    private String description;
    private Set<CommandParamInfo> params;
    private String className;
    private String executionMethod;
}
