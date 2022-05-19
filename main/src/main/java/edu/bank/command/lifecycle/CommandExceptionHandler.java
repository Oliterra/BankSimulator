package edu.bank.command.lifecycle;

import edu.bank.model.dto.Error;
import edu.bank.model.dto.ErrorMessage;
import edu.bank.model.dto.ExceptionDetails;
import edu.bank.result.CommandResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CommandExceptionHandler {

    public CommandResult<ErrorMessage> handleException(Throwable throwable) {
        Error error = getError(throwable);
        logError(error);
        List<ExceptionDetails> exceptionDetails = error.getExceptionsDetails();
        ExceptionDetails causeExceptionDetails = exceptionDetails.stream().skip(exceptionDetails.size() - 1).findFirst().get();
        ErrorMessage errorMessage = getErrorMessage(causeExceptionDetails);
        CommandResult<ErrorMessage> commandResult = new CommandResult<>();
        commandResult.setResult(errorMessage);
        return commandResult;
    }

    private Error getError(Throwable throwable) {
        Error error = new Error();
        List<ExceptionDetails> exceptionsDetails = new ArrayList<>();
        while (throwable != null) {
            ExceptionDetails exceptionDetails = new ExceptionDetails();
            exceptionDetails.setClassName(throwable.getClass().getSimpleName());
            exceptionDetails.setMessage(throwable.getMessage());
            exceptionsDetails.add(exceptionDetails);
            throwable = throwable.getCause();
        }
        error.setExceptionsDetails(exceptionsDetails);
        return error;
    }

    private ErrorMessage getErrorMessage(ExceptionDetails causeExceptionDetails) {
        ErrorMessage errorMessage;
        String exceptionClassName = causeExceptionDetails.getClassName();
        if (exceptionClassName.equals("BusinessLogicException") || exceptionClassName.equals("ValidationException")) {
            errorMessage = getCustomErrorMessage(causeExceptionDetails.getMessage());
        } else {
            errorMessage = getStandardErrorMessage();
        }
        return errorMessage;
    }

    private ErrorMessage getCustomErrorMessage(String message) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(message);
        return errorMessage;
    }

    private ErrorMessage getStandardErrorMessage() {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage("Sorry, an internal error has occurred. Try again later");
        return errorMessage;
    }

    private void logError(Error error) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("An error occurred:");
        error.getExceptionsDetails().forEach(e -> logMessage
                .append("\nException class: ")
                .append(e.getClassName())
                .append("\nException message: ")
                .append(e.getMessage()));
        log.error(logMessage.toString());
    }
}