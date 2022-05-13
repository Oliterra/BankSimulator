package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.BankFullInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.BankFullInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class GetBankCommand extends BaseCommand {

    @Override
    public CommandResult<BankFullInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException  {
        long id = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "id"));
        CommandResult<BankFullInfo> commandResult = super.executeAnyCommand(commandDescription, id);
        commandResult.setCommandResultMapper(new BankFullInfoResultMapper());
        return commandResult;
    }
}
