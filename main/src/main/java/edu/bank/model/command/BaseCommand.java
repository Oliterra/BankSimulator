package edu.bank.model.command;

import edu.bank.command.CommandParamsValidator;
import edu.bank.command.CommandsInfoStorage;
import edu.bank.exeption.BusinessLogicException;
import edu.bank.exeption.InternalError;
import edu.bank.result.CommandResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public abstract class BaseCommand implements Command {

    private ConfigurableApplicationContext context;
    private CommandParamsValidator commandParamsValidator;
    private CommandsInfoStorage commandsInfoStorage;
    private static final String SERVICE_PACKAGE_NAME = "edu.bank.service";

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
    public void validateCommand(CommandDescription commandDescription) throws IOException {
        commandParamsValidator.validateCommandParams(commandDescription);
    }

    protected <T> CommandResult<T> executeAnyCommand(CommandDescription commandDescription, Object methodParamInstance) throws ReflectiveOperationException {
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

    protected <T> CommandResult<T> executeAnyCommand(CommandDescription commandDescription) throws ReflectiveOperationException {
        CommandExecutionInfo commandExecutionInfo = getCommandExecutionInfo(commandDescription);
        Object serviceInstance = commandExecutionInfo.getServiceInstance();
        String executionMethodName = commandExecutionInfo.getExecutionMethodName();
        Method methodToExecute = serviceInstance.getClass().getMethod(executionMethodName);
        T methodResult = (T) methodToExecute.invoke(serviceInstance);
        CommandResult<T> commandResult = new CommandResult<>();
        commandResult.setResult(methodResult);
        return commandResult;
    }

    private CommandExecutionInfo getCommandExecutionInfo(CommandDescription commandDescription) {
        String commandName = commandDescription.getName();
        String executionServiceNameAndMethodName = getMethodToExecuteNameByCommandName(commandName);
        String[] splitExecutionServiceNameAndMethodName = executionServiceNameAndMethodName.split("\\.");
        if (splitExecutionServiceNameAndMethodName.length != 2)
            throw new InternalError("Incorrect execution method name:" + executionServiceNameAndMethodName);
        String executionServiceName = splitExecutionServiceNameAndMethodName[0];
        String executionMethodName = splitExecutionServiceNameAndMethodName[1];
        Class<?> serviceClassDefinition;
        try {
            serviceClassDefinition = Class.forName(SERVICE_PACKAGE_NAME + "." + executionServiceName);
        } catch (ClassNotFoundException e) {
            throw new InternalError("Invalid class name for command in commands description file");
        }
        Object serviceInstance = context.getBean(serviceClassDefinition);
        CommandExecutionInfo commandExecutionInfo = new CommandExecutionInfo();
        commandExecutionInfo.setExecutionMethodName(executionMethodName);
        commandExecutionInfo.setServiceInstance(serviceInstance);
        return commandExecutionInfo;
    }

    protected String getParamValueByNameOrThrowException(CommandDescription commandDescription, String paramName) {
        Map<String, String> commandRawParams = commandDescription.getParams();
        if (commandRawParams.containsKey(paramName)) return commandRawParams.get(paramName);
        else throw new BusinessLogicException("Invalid param name");
    }

    protected String getParamValueByNameOrReturnNull(CommandDescription commandDescription, String paramName) {
        Map<String, String> commandRawParams = commandDescription.getParams();
        return commandRawParams.getOrDefault(paramName, null);
    }

    private String getMethodToExecuteNameByCommandName(String name) {
        CommandsInfo commandsInfo = commandsInfoStorage.getCommandsInfo();
        return commandsInfo.getCommandsInfo().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .get()
                .getExecutionMethod();
    }
}
