package edu.bank.model.command;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@Component
public class CommandsInfo {

    private String commandsPackageName;
    private Set<CommandInfo> commandsInfo;
}
