package com.voting.challenge.exception;

public class MemberAlreadyVotedException extends IllegalArgumentException {

    public MemberAlreadyVotedException(String message) {
        super(message);
    }

}
