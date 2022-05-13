package edu.bank.console;

import edu.bank.exeption.InternalError;
import edu.bank.model.dto.ErrorMessage;
import edu.bank.model.enm.CommandResultStatus;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.CommandResult;
import edu.bank.result.CommandResultMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsoleCommandResultViewer {

    public <T> void showResult(CommandResult<T> commandResult) {
        T result = commandResult.getResult();
        CommandResultMapper<T> mapper = commandResult.getCommandResultMapper();
        if (result == null) {
            throw new InternalError("Result of the command execution is null");
        } else if (result.getClass().equals(ErrorMessage.class)) {
            printErrorMessage(((ErrorMessage) result));
        } else if (mapper != null) {
            printSuccessMessage(mapper.mapResult(result));
        } else {
            log.info(result.toString());
        }
    }

    private void printSuccessMessage(String result) {
        log.info(ConsoleColor.GREEN_BOLD + CommandResultStatus.SUCCESS.toString() + "\n" + result);
    }

    private void printErrorMessage(ErrorMessage errorMessage) {
        log.info(ConsoleColor.RED_BOLD + CommandResultStatus.ERROR.toString() + ": " + errorMessage.getMessage());
    }
}
