package com.voting.challenge.domain.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;

public record RegisterSessionResponse(@NonNull @NotEmpty String sessionCode) {
}
