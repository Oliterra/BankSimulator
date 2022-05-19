package edu.bank.result;

import edu.bank.result.mapper.CommandResultMapper;
import lombok.Data;

@Data
public class CommandResult<T> {

    private T result;
    private CommandResultMapper<T> commandResultMapper;
    private boolean isCycleInterrupted;
}
