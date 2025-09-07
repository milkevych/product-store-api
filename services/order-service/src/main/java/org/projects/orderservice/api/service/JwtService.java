package org.projects.orderservice.api.service;

import java.security.PublicKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.projects.orderservice.api.customer.Role;
import org.projects.orderservice.api.security.KeyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.util.CollectionUtils;

@Service
public class JwtService {

    private final PublicKey publicKey;

    public JwtService(@Value("${jwt.public-key}") String key) throws Exception {
        publicKey = KeyUtils.loadPublicKey(key);
    }

    public boolean isTokenValid(final String token, final String expectedUsername) {
        final String username = extractUsername(token);
        return username.equals(expectedUsername) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(final String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public String extractUsername(final String token) {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(final String token) {
        try {
            return Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (final JwtException e) {
            throw new RuntimeException("Invalid token type");
        }
    }

    public List<Role> getAuthorities(final String token) {
        final Claims claims = extractClaims(token);
        final List<String> authorities = (List<String>) claims.get("authorities");

        if (authorities == null) {
            return List.of();
        }

        return authorities.stream().map(Role::new).toList();
    }

}
