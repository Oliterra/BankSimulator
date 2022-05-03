package edu.bank.command.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

import static edu.bank.model.enm.ConsoleColor.BLUE_BOLD;
import static edu.bank.model.enm.ConsoleColor.RESET;

@Data
public class Command implements Cloneable {

    private String name;
    private String description;
    private Set<CommandParam> params;

    @Override
    public String toString() {
        StringBuilder paramsString = new StringBuilder();
        if (params != null) params.forEach(p -> paramsString.append(p));
        return BLUE_BOLD + name + RESET +
                " - " + description +
                "Command params list:\n" + paramsString;
    }

    @Override
    public Command clone() {
        try {
            Command clone = (Command) super.clone();
            Set<CommandParam> commandParams = clone.getParams();
            Set<CommandParam> clonedCommandParams = new HashSet<>();
            if (commandParams != null) {
                commandParams.forEach(p -> clonedCommandParams.add(p.clone()));
                clone.setParams(clonedCommandParams);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
