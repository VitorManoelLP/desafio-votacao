package com.voting.challenge.enums.converters;

import com.voting.challenge.enums.VoteOption;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class VoteOptionConverter implements AttributeConverter<VoteOption, String> {

    @Override
    public String convertToDatabaseColumn(VoteOption voteOption) {
        return voteOption.getCaption();
    }

    @Override
    public VoteOption convertToEntityAttribute(String value) {
        return VoteOption.SIM.getCaption().equals(value) ? VoteOption.SIM : VoteOption.NAO;
    }

}
