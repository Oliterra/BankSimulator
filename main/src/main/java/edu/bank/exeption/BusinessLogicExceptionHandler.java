package edu.bank.exeption;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class BusinessLogicExceptionHandler implements Thread.UncaughtExceptionHandler{
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        System.out.println(throwable.getMessage());
    }
}
