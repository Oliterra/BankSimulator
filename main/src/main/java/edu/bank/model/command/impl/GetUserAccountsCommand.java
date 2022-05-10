package edu.bank.model.command.impl;

import edu.bank.result.mapper.AccountMainInfoDTOListResultMapper;
import edu.bank.result.CommandResult;
import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.command.CommandExecutionInfo;
import edu.bank.model.command.CommandInfo;
import edu.bank.model.dto.AccountMainInfoDTO;
import edu.bank.model.dto.UserAndAccountMainInfoDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Component
public class GetUserAccountsCommand extends BaseCommand<UserAndAccountMainInfoDTO, List<AccountMainInfoDTO>> {

    @Override
    public CommandExecutionInfo<UserAndAccountMainInfoDTO> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        long userId = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "userId"));
        String currency = getParamValueByNameOrReturnNull(commandDescription, "currency");
        CommandExecutionInfo<UserAndAccountMainInfoDTO> commandExecutionInfo = new CommandExecutionInfo<>();
        UserAndAccountMainInfoDTO userAndAccountMainInfoDTO = new UserAndAccountMainInfoDTO();
        userAndAccountMainInfoDTO.setUserId(userId);
        userAndAccountMainInfoDTO.setCurrency(currency);
        commandExecutionInfo.setMethodParamInstance(userAndAccountMainInfoDTO);
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<List<AccountMainInfoDTO>> executeCommand(CommandExecutionInfo<UserAndAccountMainInfoDTO> commandExecutionInfo) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CommandResult<List<AccountMainInfoDTO>> commandResult = super.executeCommand(commandExecutionInfo);
        commandResult.setCommandResultMapper(new AccountMainInfoDTOListResultMapper());
        return commandResult;
    }
}
