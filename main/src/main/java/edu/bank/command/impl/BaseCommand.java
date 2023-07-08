package edu.bank.command.impl;

import edu.bank.command.info.Command;
import edu.bank.command.info.CommandDescription;
import edu.bank.command.info.CommandExecutionInfo;
import edu.bank.command.info.CommandsInfo;
import edu.bank.command.lifecycle.CommandParamsValidator;
import edu.bank.command.lifecycle.CommandsInfoStorage;
import edu.bank.exeption.InternalError;
import edu.bank.exeption.ValidationException;
import edu.bank.result.CommandResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

@Component
public class BaseCommand<T> implements Command<T> {

    private ConfigurableApplicationContext context;
    private CommandParamsValidator commandParamsValidator;
    private CommandsInfoStorage commandsInfoStorage;

    @Autowired
    public void setContext(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @Autowired
    public void setCommandParamsValidator(CommandParamsValidator commandParamsValidator) {
        this.commandParamsValidator = commandParamsValidator;
    }

    @Autowired
    public void setCommandsInfoStorage(CommandsInfoStorage commandsInfoStorage) {
        this.commandsInfoStorage = commandsInfoStorage;
    }

    @Override
    public void validateCommand(CommandDescription commandDescription) throws ValidationException {
        commandParamsValidator.validateCommandParams(commandDescription);
    }

    @Override
    public CommandResult<T> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        return executeAnyCommand(commandDescription);
    }

    protected CommandResult<T> executeAnyCommand(CommandDescription commandDescription, Object methodParamInstance) throws ReflectiveOperationException, ValidationException {
        CommandExecutionInfo commandExecutionInfo = getCommandExecutionInfo(commandDescription);
        Object serviceInstance = commandExecutionInfo.getServiceInstance();
        String executionMethodName = commandExecutionInfo.getExecutionMethodName();
        Class<?> methodParamClassDefinition = methodParamInstance.getClass();
        Method methodToExecute = serviceInstance.getClass().getMethod(executionMethodName, methodParamClassDefinition);
        T methodResult = (T) methodToExecute.invoke(serviceInstance, methodParamInstance);
        CommandResult<T> commandResult = new CommandResult<>();
        commandResult.setResult(methodResult);
        return commandResult;
    }

    protected String getParamValueByNameOrThrowException(CommandDescription commandDescription, String paramName) throws ValidationException {
        Map<String, String> commandRawParams = commandDescription.getParams();
        if (commandRawParams.containsKey(paramName)) return commandRawParams.get(paramName);
        else throw new ValidationException("Invalid param name");
    }

    protected String getParamValueByNameOrReturnNull(CommandDescription commandDescription, String paramName) {
        Map<String, String> commandRawParams = commandDescription.getParams();
        return commandRawParams.getOrDefault(paramName, null);
    }

    private CommandResult<T> executeAnyCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        CommandExecutionInfo commandExecutionInfo = getCommandExecutionInfo(commandDescription);
        Object serviceInstance = commandExecutionInfo.getServiceInstance();
        String executionMethodName = commandExecutionInfo.getExecutionMethodName();
        Method methodToExecute = serviceInstance.getClass().getMethod(executionMethodName);
        T methodResult = (T) methodToExecute.invoke(serviceInstance);
        CommandResult<T> commandResult = new CommandResult<>();
        commandResult.setResult(methodResult);
        return commandResult;
    }

    private CommandExecutionInfo getCommandExecutionInfo(CommandDescription commandDescription) throws ValidationException {
        String commandName = commandDescription.getName();
        String commandsExecutionServicesPackageName = commandsInfoStorage.getCommandsInfo().getCommandExecutionServicesPackageName();
        String executionServiceNameAndMethodName = getMethodToExecuteNameByCommandName(commandName);
        String[] splitExecutionServiceNameAndMethodName = executionServiceNameAndMethodName.split("\\.");
        if (splitExecutionServiceNameAndMethodName.length != 2)
            throw new InternalError("Incorrect execution method name:" + executionServiceNameAndMethodName);
        String executionServiceName = splitExecutionServiceNameAndMethodName[0];
        String executionMethodName = splitExecutionServiceNameAndMethodName[1];
        Class<?> serviceClassDefinition;
        try {
            serviceClassDefinition = Class.forName(commandsExecutionServicesPackageName + "." + executionServiceName);
        } catch (ClassNotFoundException e) {
            throw new InternalError("Invalid class name for command in commands description file: " + commandName);
        }
        Object serviceInstance = context.getBean(serviceClassDefinition);
        CommandExecutionInfo commandExecutionInfo = new CommandExecutionInfo();
        commandExecutionInfo.setExecutionMethodName(executionMethodName);
        commandExecutionInfo.setServiceInstance(serviceInstance);
        return commandExecutionInfo;
    }

    private String getMethodToExecuteNameByCommandName(String name) throws ValidationException {
        CommandsInfo commandsInfo = commandsInfoStorage.getCommandsInfo();
        return commandsInfo.getCommandsInfo().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Invalid command name"))
                .getExecutionMethod();
    }
}
