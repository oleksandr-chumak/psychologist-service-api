package com.service.psychologists.auth.services;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;

public interface JwtService {

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(String userName);

    String extractUsername(String token);

    Date extractExpiration(String token);

    Boolean validateToken(String token, UserDetails userDetails);
}
