package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.IndividualFullInfo;
import edu.bank.model.dto.IndividualToCreate;
import edu.bank.model.entity.Individual;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.IndividualFullInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class CreateIndividualCommand extends BaseCommand {

    @Override
    public CommandResult<IndividualFullInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException  {
        IndividualToCreate individualToCreate = getIndividualToCreateDTO(commandDescription);
        CommandResult<IndividualFullInfo> commandResult = super.executeAnyCommand(commandDescription, individualToCreate);
        commandResult.setCommandResultMapper(new IndividualFullInfoResultMapper());
        return commandResult;
    }

    private IndividualToCreate getIndividualToCreateDTO(CommandDescription commandDescription) {
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
        IndividualToCreate individualToCreate = new IndividualToCreate();
        individualToCreate.setBankId(bankId);
        individualToCreate.setIndividual(individual);
        return individualToCreate;
    }
}
