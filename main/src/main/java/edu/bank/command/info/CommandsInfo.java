package edu.bank.command.info;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@Component
public class CommandsInfo {

    private String commandsPackageName;
    private String commandExecutionServicesPackageName;
    private Set<CommandInfo> commandsInfo;
}
