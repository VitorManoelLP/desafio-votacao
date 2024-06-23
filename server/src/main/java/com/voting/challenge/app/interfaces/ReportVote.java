package com.voting.challenge.app.interfaces;

import com.voting.challenge.domain.payload.CountReport;
import jakarta.validation.constraints.NotBlank;

public interface ReportVote {
    CountReport count(@NotBlank String code);
}
