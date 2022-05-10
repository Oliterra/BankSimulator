package edu.bank.command;

import edu.bank.result.CommandResult;
import edu.bank.exeption.BusinessLogicException;
import edu.bank.model.dto.ErrorDTO;
import edu.bank.model.enm.ConsoleColor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class CommandExceptionHandler {

    private Map<Class<? extends Throwable>, String> exceptionHandlingMap;

    @PostConstruct
    private void init() {
        exceptionHandlingMap = new HashMap<>();
        exceptionHandlingMap.put(IOException.class, "Invalid command format. To view all commands use \"help\"");
        exceptionHandlingMap.put(NumberFormatException.class, "Invalid param format. To view all commands params info use \"help\"");
    }

    public CommandResult<ErrorDTO> handleException(Throwable cause) {
        ErrorDTO errorDTO;
        if (cause != null) {
            logError(cause);
            Class<? extends Throwable> causeClass = cause.getClass();
            String causeMessage = cause.getMessage();
            errorDTO = formErrorDTO(causeClass, causeMessage);
        } else errorDTO = formStandardErrorMessage();
        CommandResult<ErrorDTO> commandResult = new CommandResult<>();
        commandResult.setResult(errorDTO);
        return commandResult;
    }

    private void logError(Throwable cause) {
        log.error(ConsoleColor.RED_BOLD + "An error occurred: class:{}, message: {}",
                cause.getClass().getSimpleName(), cause.getMessage() + ConsoleColor.RESET);
    }

    private ErrorDTO formErrorDTO(Class<? extends Throwable> causeClass, String causeMessage) {
        if (causeClass.equals(BusinessLogicException.class)) return handleBusinessLogicException(causeMessage);
        else if (exceptionHandlingMap.containsKey(causeClass)) return formCustomErrorMessage(causeMessage);
        else return formStandardErrorMessage();
    }

    private ErrorDTO handleBusinessLogicException(String message) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(message);
        return errorDTO;
    }

    private ErrorDTO formCustomErrorMessage(String message) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(message);
        return errorDTO;
    }

    private ErrorDTO formStandardErrorMessage() {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage("Sorry, an internal error has occurred. Try again later");
        return errorDTO;
    }
}