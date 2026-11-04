package net.aouchar.accountservice.exceptions;

public class BalanceNotSufficientException extends RuntimeException{
    public BalanceNotSufficientException(String message){
        super(message);
    }
}
