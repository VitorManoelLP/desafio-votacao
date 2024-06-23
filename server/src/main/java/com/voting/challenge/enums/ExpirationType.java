package com.voting.challenge.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Getter
@RequiredArgsConstructor
public enum ExpirationType {

    MINUTES((value, date) -> date.plusMinutes(value)),
    HOURS((value, date) -> date.plusHours(value)),
    DAYS((value, date) -> date.plusDays(value));

    private final BiFunction<Long, LocalDateTime, LocalDateTime> applyExpirationOnDate;
}
