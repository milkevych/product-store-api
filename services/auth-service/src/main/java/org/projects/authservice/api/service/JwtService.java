package org.projects.authservice.api.service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.projects.authservice.api.security.KeyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    private static final String TOKEN_TYPE = "token_type";
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    
    @Value("${app.security.jwt.access-token-expiration}")
    private long accessTokenExpiration;
    @Value("${app.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public JwtService() throws Exception {
        this.privateKey = KeyUtils.loadPrivateKey("keys/local-only/private_key.pem");
        this.publicKey = KeyUtils.loadPublicKey("keys/local-only/public_key.pem");
    }

    public String generateAccessToken(final String username, final List<String> authorities) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_TYPE, "ACCESS_TOKEN");
        return buildToken(username, claims, accessTokenExpiration, authorities);
    }

    public String generateRefreshToken(final String username, List<String> authorities) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_TYPE, "REFRESH_TOKEN");
        return buildToken(username, claims, refreshTokenExpiration, authorities);
    }

    public boolean isTokenValid(final String token, final String expectedUsername) {
        final String username= extractUsername(token);
        return username.equals(expectedUsername) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(final String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public String buildToken(String username, Map<String, Object> claims, long expiration, final List<String> authorities) {
        claims.put("authorities", authorities);
        return Jwts.builder()
                    .claims(claims)
                    .subject(username)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(privateKey)
                    .compact();
    }

    public String extractUsername(final String token) {
        return extractClaims(token).getSubject();
    }

    public List<String> extractAuthorities(final String token) {
        return (List<String>)extractClaims(token).get("authorities");
    }

    private Claims extractClaims(final String token) {
        try {
            return Jwts.parser()
                        .verifyWith(publicKey)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
        } catch(final JwtException e) {
            throw new RuntimeException("Invalid token type");
        }
    }

    public String refreshAccessToken(final String refreshToken) {
        final Claims claims = extractClaims(refreshToken);
        if (!"REFRESH_TOKEN".equals(claims.get(TOKEN_TYPE))) {
            throw new RuntimeException("Invalid token type");
        }

        if(isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh token expired");
        }

        List<String> authorities = (List<String>) claims.get("authorities");

        final String username= claims.getSubject();
        return generateAccessToken(username, authorities);
    }

}
