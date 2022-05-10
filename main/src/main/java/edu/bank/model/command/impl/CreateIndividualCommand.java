package edu.bank.model.command.impl;

import edu.bank.result.CommandResult;
import edu.bank.result.mapper.IndividualFullInfoDTOResultMapper;
import edu.bank.model.command.*;
import edu.bank.model.dto.IndividualFullInfoDTO;
import edu.bank.model.dto.IndividualToCreateDTO;
import edu.bank.model.entity.Individual;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class CreateIndividualCommand extends BaseCommand<IndividualToCreateDTO, IndividualFullInfoDTO> {

    @Override
    public CommandExecutionInfo<IndividualToCreateDTO> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        long bankId = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "bankId"));
        String firstName = getParamValueByNameOrThrowException(commandDescription, "firstName");
        String lastName = getParamValueByNameOrThrowException(commandDescription, "lastName");
        String patronymic = getParamValueByNameOrThrowException(commandDescription, "patronymic");
        String phone = getParamValueByNameOrThrowException(commandDescription, "phone");
        Individual individual = new Individual();
        individual.setName(firstName);
        individual.setLastName(lastName);
        individual.setPatronymic(patronymic);
        individual.setPhone(phone);
        IndividualToCreateDTO individualToCreateDTO = new IndividualToCreateDTO();
        individualToCreateDTO.setBankId(bankId);
        individualToCreateDTO.setIndividual(individual);
        CommandExecutionInfo<IndividualToCreateDTO> commandExecutionInfo = new CommandExecutionInfo<>();
        commandExecutionInfo.setMethodParamInstance(individualToCreateDTO);
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<IndividualFullInfoDTO> executeCommand(CommandExecutionInfo<IndividualToCreateDTO> commandExecutionInfo) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        CommandResult<IndividualFullInfoDTO> commandResult = super.executeCommand(commandExecutionInfo);
        commandResult.setCommandResultMapper(new IndividualFullInfoDTOResultMapper());
        return commandResult;
    }
}
