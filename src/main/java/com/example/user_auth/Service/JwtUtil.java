package com.example.user_auth.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtUtil {
    private static final String SECRET_KEY = "super_secret_key_12345_this_key_is_now_long_enough_for_hmac_sha256_algorithm";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public static String generateToken(String phone) {
        long expirationTime = 24 * 60 * 60 * 1000; // 1 kun
        return Jwts.builder()
                .setSubject(phone)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
