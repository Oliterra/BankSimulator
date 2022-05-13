package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.result.CommandResult;
import org.springframework.stereotype.Component;

@Component
public class WelcomeCommand extends BaseCommand {

    @Override
    public CommandResult<String> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException  {
        return super.executeAnyCommand(commandDescription);
    }
}
