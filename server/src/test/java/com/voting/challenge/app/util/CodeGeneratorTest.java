package com.voting.challenge.app.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CodeGeneratorTest {

    @Test
    public void ensureRandomness() {
        final List<String> keys = new ArrayList<>();
        for (int index = 0; index < 100; index++) {
            keys.add(CodeGenerator.generateKey());
        }
        Assertions.assertThat(keys).doesNotHaveDuplicates();
    }

}