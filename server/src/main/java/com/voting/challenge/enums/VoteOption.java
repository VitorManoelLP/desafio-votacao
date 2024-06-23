package com.voting.challenge.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VoteOption {

    SIM("Sim"), NAO("Não");

    private final String caption;

    @JsonValue
    public String getCaption() {
        return caption;
    }

    @JsonCreator
    public static VoteOption fromCaption(String caption) {
        for (VoteOption option : VoteOption.values()) {
            if (option.getCaption().equalsIgnoreCase(caption)) {
                return option;
            }
        }
        throw new IllegalArgumentException("Unknown caption: " + caption);
    }

}
