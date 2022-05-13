package edu.bank.service;

import edu.bank.command.CommandsInfoStorage;
import edu.bank.model.command.CommandsInfo;
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

    public Set<CommandFullInfo> getAllCommands() {
        CommandsInfo commandsInfo = commandsInfoStorage.getCommandsInfo();
        return commandsInfo.getCommandsInfo().stream().map(c -> modelMapper.map(c, CommandFullInfo.class)).collect(Collectors.toSet());
    }

    public String getWelcomeMessage() {
        return (ConsoleColor.MAGENTA_BOLD + "Welcome!\nTo view a list of all available commands, type \"help\"." +
                "\nTo exit the application, use \"exit\"." + ConsoleColor.RESET);
    }
}
