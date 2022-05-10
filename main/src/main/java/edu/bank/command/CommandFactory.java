package edu.bank.command;

import edu.bank.model.command.Command;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.command.CommandInfo;
import edu.bank.model.command.CommandsInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Component
@Data
@RequiredArgsConstructor
public class CommandFactory {

    private CommandsInfo commandsInfo;
    private ApplicationContext context;

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    private void init() {
        Yaml yaml = new Yaml(new Constructor(CommandsInfo.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("commands-list.yml");
        commandsInfo = yaml.load(inputStream);
        if (commandsInfo.getCommandsInfo().isEmpty()) throw new RuntimeException();
    }

    public <T, E> Command<T, E> createCommandByDescription(CommandDescription commandDescription) throws Exception {
        String commandName = commandDescription.getName();
        String commandsPackageName = commandsInfo.getCommandsPackageName();
        CommandInfo commandInfo = getCommandInfoByCommandName(commandName);
        String commandClassName = commandInfo.getClassName();
        Class<?> classDefinition = Class.forName(commandsPackageName + "." + commandClassName);
        Command<T, E> commandInstance = (Command<T, E>) context.getBean(classDefinition);
        commandInstance.validateCommand(commandDescription, commandInfo);
        return commandInstance;
    }

    public CommandInfo getCommandInfoByCommandName(String commandName) throws IOException {
        return commandsInfo.getCommandsInfo().stream()
                .filter(c -> c.getName().equals(commandName))
                .findFirst()
                .orElseThrow(IOException::new);
    }
}

