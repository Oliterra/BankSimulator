package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.AccountMainInfo;
import edu.bank.model.dto.UserAndAccountMainInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.AccountMainInfoListResultMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetUserAccountsCommand extends BaseCommand {

    @Override
    public CommandResult<List<AccountMainInfo>> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException  {
        UserAndAccountMainInfo userAndAccountMainInfo = getUserAndAccountMainInfo(commandDescription);
        CommandResult<List<AccountMainInfo>> commandResult = super.executeAnyCommand(commandDescription, userAndAccountMainInfo);
        commandResult.setCommandResultMapper(new AccountMainInfoListResultMapper());
        return commandResult;
    }

    private UserAndAccountMainInfo getUserAndAccountMainInfo(CommandDescription commandDescription) {
        long userId = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "userId"));
        String currency = getParamValueByNameOrReturnNull(commandDescription, "currency");
        UserAndAccountMainInfo userAndAccountMainInfo = new UserAndAccountMainInfo();
        userAndAccountMainInfo.setUserId(userId);
        userAndAccountMainInfo.setCurrency(currency);
        return userAndAccountMainInfo;
    }
}
