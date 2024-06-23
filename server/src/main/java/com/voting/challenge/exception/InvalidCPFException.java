package com.voting.challenge.exception;

public class InvalidCPFException extends IllegalArgumentException {
    public InvalidCPFException(String message){
        super(message);
    }
}
