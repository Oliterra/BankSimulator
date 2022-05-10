package edu.bank.console;

import edu.bank.model.dto.ErrorDTO;
import edu.bank.model.enm.CommandResultStatus;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.CommandResult;
import edu.bank.result.CommandResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsoleCommandResultViewer {

    public void showWelcomeMessage() {
        System.out.println("""
                Welcome!
                To view a list of all available commands, type "help".
                To exit the application, use "exit".""");
    }

    public boolean isTheCycleInterrupted(String consoleInput) {
        return consoleInput.equals("exit");
    }

    public <T> void showResult(CommandResult<T> commandResult) {
        T result = commandResult.getResult();
        CommandResultMapper<T> mapper = commandResult.getCommandResultMapper();
        if (result == null) printSuccessMessage();
        else if (mapper != null) mapper.showInfo(result);
        else if (result.getClass().equals(ErrorDTO.class)) printErrorMessage(((ErrorDTO) result).getMessage());
        else System.out.println(result);
    }

    private void printSuccessMessage(){
        System.out.println(ConsoleColor.GREEN_BOLD + CommandResultStatus.SUCCESS.toString());
    }

    private void printErrorMessage(String errorCause){
        System.out.println(ConsoleColor.RED_BOLD + CommandResultStatus.ERROR.toString() + ": " + errorCause);
    }
}
