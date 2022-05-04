package edu.bank.command.model;

import edu.bank.model.enm.CommandResultStatus;
import edu.bank.model.enm.ConsoleColor;
import lombok.AllArgsConstructor;
import lombok.Data;

import static edu.bank.model.enm.ConsoleColor.*;

@Data
@AllArgsConstructor
public class CommandResult {

    private String message;
    private CommandResultStatus status;
    private ConsoleColor color;

    @Override
    public String toString() {
        return "\t" + color + status + "!\n" +  message +  RESET + "\n";
    }
}
