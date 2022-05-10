package edu.bank.model.command;

import lombok.Data;

@Data
public class CommandExecutionInfo<T> {

    T methodParamInstance;
    CommandInfo commandInfo;
}
