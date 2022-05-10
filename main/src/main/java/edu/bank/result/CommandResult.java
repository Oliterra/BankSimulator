package edu.bank.result;

import lombok.Data;

@Data
public class CommandResult<T> {

    private T result;
    private CommandResultMapper<T> commandResultMapper;
}
