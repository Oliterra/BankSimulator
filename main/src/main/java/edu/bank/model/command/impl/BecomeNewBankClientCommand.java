package edu.bank.model.command.impl;

import edu.bank.result.mapper.AccountMainInfoDTOResultMapper;
import edu.bank.result.CommandResult;
import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.command.CommandExecutionInfo;
import edu.bank.model.command.CommandInfo;
import edu.bank.model.dto.AccountMainInfoDTO;
import edu.bank.model.dto.CreateBankClientDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class BecomeNewBankClientCommand extends BaseCommand<CreateBankClientDTO, AccountMainInfoDTO> {

    @Override
    public CommandExecutionInfo<CreateBankClientDTO> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        long userId = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "userId"));
        long bankId = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "bankId"));
        CommandExecutionInfo<CreateBankClientDTO> commandExecutionInfo = new CommandExecutionInfo<>();
        CreateBankClientDTO createBankClientDTO = new CreateBankClientDTO();
        createBankClientDTO.setUserId(userId);
        createBankClientDTO.setBankId(bankId);
        commandExecutionInfo.setMethodParamInstance(createBankClientDTO);
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<AccountMainInfoDTO> executeCommand(CommandExecutionInfo<CreateBankClientDTO> commandExecutionInfo) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CommandResult<AccountMainInfoDTO> commandResult = super.executeCommand(commandExecutionInfo);
        commandResult.setCommandResultMapper(new AccountMainInfoDTOResultMapper());
        return commandResult;
    }
}
