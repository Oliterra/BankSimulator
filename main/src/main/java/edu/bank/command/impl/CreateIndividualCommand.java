package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.model.dto.IndividualFullInfo;
import edu.bank.model.dto.IndividualToCreate;
import edu.bank.model.entity.Individual;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.impl.IndividualFullInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class CreateIndividualCommand extends BaseCommand<IndividualFullInfo> {

    @Override
    public CommandResult<IndividualFullInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        IndividualToCreate individualToCreate = getIndividualToCreateDTO(commandDescription);
        CommandResult<IndividualFullInfo> commandResult = executeAnyCommand(commandDescription, individualToCreate);
        commandResult.setCommandResultMapper(new IndividualFullInfoResultMapper());
        return commandResult;
    }

    private IndividualToCreate getIndividualToCreateDTO(CommandDescription commandDescription) throws ValidationException {
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
