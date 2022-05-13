package edu.bank.command;

import edu.bank.model.command.Command;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.command.CommandsInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

    public Command createCommand(CommandDescription commandDescription) throws IOException, ClassNotFoundException {
        String commandsPackageName = commandsInfoStorage.getCommandsInfo().getCommandsPackageName();
        String commandName = commandDescription.getName();
        String commandClassName = getCommandClassByNameIfExists(commandName);
        Class<?> classDefinition = Class.forName(commandsPackageName + "." + commandClassName);
        return (Command) context.getBean(classDefinition);
    }

    public String getCommandClassByNameIfExists(String name) throws IOException {
        CommandsInfo commandsInfo = commandsInfoStorage.getCommandsInfo();
        return commandsInfo.getCommandsInfo().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IOException("There is no such command"))
                .getClassName();
    }
}

