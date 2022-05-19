package edu.bank.service;

import edu.bank.command.lifecycle.CommandsInfoStorage;
import edu.bank.command.info.CommandsInfo;
import edu.bank.model.dto.CommandFullInfo;
import edu.bank.model.enm.ConsoleColor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandService {

    private final CommandsInfoStorage commandsInfoStorage;
    private final ModelMapper modelMapper;
    private static final String WELCOME_CONSOLE_MESSAGE = ConsoleColor.MAGENTA_BOLD + "Welcome!\nTo view a list of all available commands, type \"help\"." +
            "\nTo exit the application, use \"exit\"." + ConsoleColor.RESET;
    private static final String GOODBYE_CONSOLE_MESSAGE = ConsoleColor.MAGENTA_BOLD + "Good bye!\nCome again" + ConsoleColor.RESET;

    public Set<CommandFullInfo> getAllCommands() {
        CommandsInfo commandsInfo = commandsInfoStorage.getCommandsInfo();
        return commandsInfo.getCommandsInfo().stream().map(c -> modelMapper.map(c, CommandFullInfo.class)).collect(Collectors.toSet());
    }

    public String getWelcomeMessage() {
        return WELCOME_CONSOLE_MESSAGE;
    }

    public String getGoodbyeMessage() {
        return GOODBYE_CONSOLE_MESSAGE;
    }
}
