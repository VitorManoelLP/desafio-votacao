package com.voting.challenge.infra.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public final class JwtTokenValidator {

    public static final String JWT_KEY = "MxiWmFvKKdJ6APJWS57Es3CHCiRj76EGKUimExc9D6RfSkQYUbqHni7arKZVKg2tYMyrTVDBUy8RsohurxCESpd";

    private JwtTokenValidator() {
    }

    public static String generateToken(UserDetails userDetails) {
        Map<String, String> claims = new HashMap<>();
        return createToken(claims, userDetails);
    }

    private static String createToken(Map<String, String> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
                .signWith(Keys.hmacShaKeyFor(generateSafeSecret()))
                .compact();
    }

    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static Boolean isValidToken(String token, UserDetails userDetails) {
        final String username = getUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public static String getUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKeyResolver(new KeyResolver()).build().parseClaimsJws(token).getBody();
    }

    public static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private static byte[] generateSafeSecret() {
        byte[] bytes = JWT_KEY.getBytes();
        var encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encode(bytes);
    }

}