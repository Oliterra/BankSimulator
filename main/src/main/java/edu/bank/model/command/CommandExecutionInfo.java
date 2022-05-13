package edu.bank.model.command;

import lombok.Data;

@Data
public class CommandExecutionInfo {

    private Object serviceInstance;
    private String executionMethodName;
}
