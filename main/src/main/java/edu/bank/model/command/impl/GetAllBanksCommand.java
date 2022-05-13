package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.BankFullInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.BankFullInfoListResultMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllBanksCommand extends BaseCommand {

    @Override
    public CommandResult<List<BankFullInfo>> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException  {
        CommandResult<List<BankFullInfo>> commandResult = super.executeAnyCommand(commandDescription);
        commandResult.setCommandResultMapper(new BankFullInfoListResultMapper());
        return commandResult;
    }
}
