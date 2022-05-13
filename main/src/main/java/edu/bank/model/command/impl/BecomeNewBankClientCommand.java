package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.AccountMainInfo;
import edu.bank.model.dto.CreateBankClient;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.AccountMainInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class BecomeNewBankClientCommand extends BaseCommand {

    @Override
    public CommandResult<AccountMainInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException {
        CreateBankClient createBankClient = getCreateBankClientDTO(commandDescription);
        CommandResult<AccountMainInfo> commandResult = super.executeAnyCommand(commandDescription, createBankClient);
        commandResult.setCommandResultMapper(new AccountMainInfoResultMapper());
        return commandResult;
    }

    private CreateBankClient getCreateBankClientDTO(CommandDescription commandDescription) {
        long userId = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "userId"));
        long bankId = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "bankId"));
        CreateBankClient createBankClient = new CreateBankClient();
        createBankClient.setUserId(userId);
        createBankClient.setBankId(bankId);
        return createBankClient;
    }
}
