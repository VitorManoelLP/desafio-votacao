package com.voting.challenge.app.util;

import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    private SecurityUtil() {}

    public static String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
