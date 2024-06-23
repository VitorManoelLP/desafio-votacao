package com.voting.challenge.exception;

public class PermissionDeniedVotingSessionView extends IllegalArgumentException {

    public PermissionDeniedVotingSessionView(String message) {
        super(message);
    }

}
