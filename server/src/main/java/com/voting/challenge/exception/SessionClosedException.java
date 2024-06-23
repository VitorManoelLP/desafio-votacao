package com.voting.challenge.exception;

public class SessionClosedException extends IllegalArgumentException {
    public SessionClosedException(String message) {
        super(message);
    }
}
