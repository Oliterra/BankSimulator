package edu.bank.command;

import edu.bank.command.model.Command;
import edu.bank.command.model.CommandList;
import edu.bank.command.model.CommandParam;
import edu.bank.console.ConsoleLineParser;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@Component
@Data
@RequiredArgsConstructor
public class CommandFactoryImpl implements CommandFactory {

    private final CommandInspector commandInspector;
    private final CommandParamInspectorImpl commandParamInspector;
    private final ConsoleLineParser consoleLineParser;
    private CommandList commandList;

    @PostConstruct
    private void init() {
        Yaml yaml = new Yaml(new Constructor(CommandList.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("commands-list.yml");
        commandList = yaml.load(inputStream);
        if (commandList.getCommands().isEmpty()) throw new RuntimeException();
    }

    @Override
    public Command getFullCommandByConsoleInput(String consoleInput) throws IOException {
        String commandName = consoleLineParser.getCommandNameFromConsoleInput(consoleInput);
        Command command = getCommandByNameOrThrowException(commandName);
        Command newCommand = createNewCommandInstance(command);
        setCommandParamsValues(newCommand, consoleInput);
        return newCommand;
    }

    @Override
    public Command getCommandByNameOrThrowException(String name) throws IOException {
        Command command = commandList.getCommands().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElseThrow(IOException::new);
        if (commandInspector.isAllCommandInfoPresents(command)) return command;
        else throw new RuntimeException();
    }

    private void setCommandParamsValues(Command command, String consoleInput) throws IOException {
        String[] consoleInputParts = consoleLineParser.parseConsoleInput(consoleInput);
        for (int i = 1; i < consoleInputParts.length; i++) {
            String paramAndValue = consoleInputParts[i];
            if (!commandParamInspector.areCommandParamAndValueValid(paramAndValue)) throw new IOException();
            String[] splittedParamAndValue = paramAndValue.split("=");
            if (splittedParamAndValue.length != 2) throw new IOException();
            String name = splittedParamAndValue[0].substring(1);
            String value = splittedParamAndValue[1];
            setCommandParamsValuesIfParamPresentOrThrowException(command.getParams(), name, value);
        }
    }

    private void setCommandParamsValuesIfParamPresentOrThrowException(Set<CommandParam> params, String name, String value) throws IOException {
        for (CommandParam commandParam : params) {
            if (!commandParam.getName().equals(name)) continue;
            if (commandParam.getValue() == null) commandParam.setValue(value);
            else throw new IOException();
        }
    }

    private Command createNewCommandInstance(Command command) {
        return command.clone();
    }
}
