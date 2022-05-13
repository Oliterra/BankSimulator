package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.CommandFullInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.CommandFullInfoListResultMapper;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class HelpCommand extends BaseCommand {

    @Override
    public CommandResult<Set<CommandFullInfo>> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException  {
        CommandResult<Set<CommandFullInfo>> commandResult = super.executeAnyCommand(commandDescription);
        commandResult.setCommandResultMapper(new CommandFullInfoListResultMapper());
        return commandResult;
    }
}
