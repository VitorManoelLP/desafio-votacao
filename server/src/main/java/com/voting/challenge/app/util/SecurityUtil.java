package com.voting.challenge.app.util;

import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    private SecurityUtil() {}

    public static String getCPF() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
