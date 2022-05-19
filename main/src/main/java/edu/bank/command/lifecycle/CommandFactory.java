package edu.bank.command.lifecycle;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.Command;
import edu.bank.command.info.CommandDescription;
import edu.bank.command.info.CommandsInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Data
@RequiredArgsConstructor
public class CommandFactory {

    private ApplicationContext context;
    private final CommandsInfoStorage commandsInfoStorage;

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }

    public Command createCommand(CommandDescription commandDescription) throws ClassNotFoundException, ValidationException {
        String commandsPackageName = commandsInfoStorage.getCommandsInfo().getCommandsPackageName();
        String commandName = commandDescription.getName();
        String commandClassName = getCommandClassByNameIfExists(commandName);
        Class<?> classDefinition = Class.forName(commandsPackageName + "." + commandClassName);
        return (Command) context.getBean(classDefinition);
    }

    public String getCommandClassByNameIfExists(String name) throws ValidationException {
        CommandsInfo commandsInfo = commandsInfoStorage.getCommandsInfo();
        return commandsInfo.getCommandsInfo().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new ValidationException("There is no such command"))
                .getClassName();
    }
}

