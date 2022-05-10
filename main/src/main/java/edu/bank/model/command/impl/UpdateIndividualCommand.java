package edu.bank.model.command.impl;

import edu.bank.result.CommandResult;
import edu.bank.model.command.*;
import edu.bank.model.dto.IndividualToUpdateDTO;
import edu.bank.model.entity.Individual;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class UpdateIndividualCommand extends BaseCommand<IndividualToUpdateDTO, Void> {

    @Override
    public CommandExecutionInfo<IndividualToUpdateDTO> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        long id = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "id"));
        String firstName = getParamValueByNameOrReturnNull(commandDescription, "firstName");
        String lastName = getParamValueByNameOrReturnNull(commandDescription, "lastName");
        String patronymic = getParamValueByNameOrReturnNull(commandDescription, "patronymic");
        String phone = getParamValueByNameOrReturnNull(commandDescription, "phone");
        Individual individualToUpdate = new Individual();
        individualToUpdate.setName(firstName);
        individualToUpdate.setLastName(lastName);
        individualToUpdate.setPatronymic(patronymic);
        individualToUpdate.setPhone(phone);
        IndividualToUpdateDTO individualToUpdateDTO = new IndividualToUpdateDTO();
        individualToUpdateDTO.setId(id);
        individualToUpdateDTO.setIndividual(individualToUpdate);
        CommandExecutionInfo<IndividualToUpdateDTO> commandExecutionInfo = new CommandExecutionInfo<>();
        commandExecutionInfo.setMethodParamInstance(individualToUpdateDTO);
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<Void> executeCommand(CommandExecutionInfo<IndividualToUpdateDTO> commandExecutionInfo) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return super.executeCommand(commandExecutionInfo);
    }
}
