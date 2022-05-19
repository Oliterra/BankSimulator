package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.model.dto.IndividualToUpdate;
import edu.bank.model.entity.Individual;
import edu.bank.result.CommandResult;
import org.springframework.stereotype.Component;

@Component
public class UpdateIndividualCommand extends BaseCommand<String> {

    @Override
    public CommandResult<String> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        IndividualToUpdate individualToUpdate = getIndividualToUpdate(commandDescription);
        return executeAnyCommand(commandDescription, individualToUpdate);
    }

    private IndividualToUpdate getIndividualToUpdate(CommandDescription commandDescription) throws ValidationException {
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
        IndividualToUpdate individualToUpdateDTO = new IndividualToUpdate();
        individualToUpdateDTO.setId(id);
        individualToUpdateDTO.setIndividual(individualToUpdate);
        return individualToUpdateDTO;
    }
}
