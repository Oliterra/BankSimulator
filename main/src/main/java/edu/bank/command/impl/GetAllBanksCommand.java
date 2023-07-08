package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.model.dto.BankFullInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.impl.BankFullInfoListResultMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllBanksCommand extends BaseCommand<List<BankFullInfo>> {

    @Override
    public CommandResult<List<BankFullInfo>> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        CommandResult<List<BankFullInfo>> commandResult = super.executeCommand(commandDescription);
        commandResult.setCommandResultMapper(new BankFullInfoListResultMapper());
        return commandResult;
    }
}
