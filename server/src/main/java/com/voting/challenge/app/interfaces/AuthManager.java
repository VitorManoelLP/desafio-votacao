package com.voting.challenge.app.interfaces;

import com.voting.challenge.domain.payload.CreateUserRequest;
import com.voting.challenge.domain.payload.LoginRequest;
import com.voting.challenge.domain.payload.MemberInfo;
import com.voting.challenge.domain.payload.TokenResponse;

public interface AuthManager {
    TokenResponse create(CreateUserRequest request);
    TokenResponse login(LoginRequest loginRequest);
    MemberInfo info(String cpf);
}
