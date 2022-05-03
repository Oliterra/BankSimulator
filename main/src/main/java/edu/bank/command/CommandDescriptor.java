package edu.bank.console;

import edu.bank.command.Command;
import edu.bank.command.CommandFactory;
import edu.bank.command.CommandImpl;
import edu.bank.command.CommandsList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ConsoleDescriptor {

    private final CommandFactory commandFactory;

    public void showAllCommandsWithDescriptions(){
        CommandsList commands = commandFactory.getCommandsList();
        System.out.println(commands);
    }
}
