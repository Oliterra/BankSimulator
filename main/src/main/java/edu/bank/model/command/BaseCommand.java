package edu.bank.model.command;

import edu.bank.command.CommandParamsValidator;
import edu.bank.result.CommandResult;
import edu.bank.exeption.BusinessLogicException;
import edu.bank.exeption.InternalError;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Component
@RequiredArgsConstructor
public abstract class BaseCommand<T, E> implements Command<T, E> {

    private ApplicationContext context;

    private CommandParamsValidator commandParamsValidator;

    private static final String SERVICE_PACKAGE_NAME = "edu.bank.service";

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Autowired
    public void setCommandParamsValidator(CommandParamsValidator commandParamsValidator) {
        this.commandParamsValidator = commandParamsValidator;
    }

    @Override
    public void validateCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        commandParamsValidator.validateCommandParams(commandInfo, commandDescription);
    }

    @Override
    public abstract CommandExecutionInfo<T> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo);

    public CommandResult<E> executeCommand(CommandExecutionInfo<T> commandExecutionInfo) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CommandInfo commandInfo = commandExecutionInfo.getCommandInfo();
        String executionServiceNameAndMethodName = commandInfo.getExecutionMethod();
        String[] splitExecutionServiceNameAndMethodName = executionServiceNameAndMethodName.split("\\.");
        if (splitExecutionServiceNameAndMethodName.length != 2)
            throw new InternalError("Incorrect execution method name:" + executionServiceNameAndMethodName);
        String executionServiceName = splitExecutionServiceNameAndMethodName[0];
        String executionMethodName = splitExecutionServiceNameAndMethodName[1];
        Class<?> serviceClassDefinition = Class.forName(SERVICE_PACKAGE_NAME + "." + executionServiceName);
        Object serviceInstance = context.getBean(serviceClassDefinition);
        E methodResult;
        Method methodToExecute;
        if (commandExecutionInfo.getMethodParamInstance() != null) {
            Class<?> methodParamClassDefinition = commandExecutionInfo.getMethodParamInstance().getClass();
            methodToExecute = serviceInstance.getClass().getMethod(executionMethodName, methodParamClassDefinition);
            T methodToExecuteParam = commandExecutionInfo.getMethodParamInstance();
            methodResult = (E) methodToExecute.invoke(serviceInstance, methodToExecuteParam);
        } else {
            methodToExecute = serviceInstance.getClass().getMethod(executionMethodName);
            methodResult = (E) methodToExecute.invoke(serviceInstance);
        }
        CommandResult<E> commandResult = new CommandResult<>();
        commandResult.setResult(methodResult);
        return commandResult;
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
}
