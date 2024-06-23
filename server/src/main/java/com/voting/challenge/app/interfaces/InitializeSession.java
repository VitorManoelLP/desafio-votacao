package com.voting.challenge.app.interfaces;

import com.voting.challenge.domain.payload.RegisterSessionDTO;
import com.voting.challenge.domain.payload.RegisterSessionResponse;
import jakarta.validation.constraints.NotNull;

public interface InitializeSession {
    RegisterSessionResponse init(@NotNull RegisterSessionDTO registerSessionDTO);
}
