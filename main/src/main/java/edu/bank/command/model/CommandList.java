package edu.bank.command.model;

import lombok.Data;

import java.util.List;

@Data
public class CommandList {

    private List<Command> commands;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        commands.forEach(c -> result.append(c.toString()).append("\n"));
        return "All commands:\n" + result.toString();
    }
}
