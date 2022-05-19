package edu.bank.command.info;

import lombok.Data;

@Data
public class CommandExecutionInfo {

    private Object serviceInstance;
    private String executionMethodName;
}
