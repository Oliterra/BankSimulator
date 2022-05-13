package edu.bank.result;

public interface CommandResultMapper<T> {

    String mapResult(T result);
}
