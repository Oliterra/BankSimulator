package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.IndividualToUpdate;
import edu.bank.model.entity.Individual;
import edu.bank.result.CommandResult;
import org.springframework.stereotype.Component;

@Component
public class UpdateIndividualCommand extends BaseCommand {

    @Override
    public CommandResult<String> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException  {
        IndividualToUpdate individualToUpdate = getIndividualToUpdate(commandDescription);
        return super.executeAnyCommand(commandDescription, individualToUpdate);
    }

    private IndividualToUpdate getIndividualToUpdate(CommandDescription commandDescription) {
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
