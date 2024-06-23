package com.voting.challenge.app.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Random;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class CodeGenerator {

    public static String generateKey() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            if (i > 0 && i % 4 == 0) {
                key.append("-");
            }
            key.append(random.nextInt(10));
        }
        return key.toString();
    }

}
