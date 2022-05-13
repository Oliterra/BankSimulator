package edu.bank.model.command.impl;

import edu.bank.exeption.ExitRequest;
import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.result.CommandResult;
import org.springframework.stereotype.Component;

@Component
public class ExitCommand extends BaseCommand {

    @Override
    public CommandResult<String> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException  {
        throw new ExitRequest();
    }
}
