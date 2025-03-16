package com.backend.globeonclick.configuration.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        String userType = getUserType(userDetails);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        claims.put("type", userType);

        // Agregar información específica según el tipo de usuario
        if (userDetails instanceof com.backend.globeonclick.entity.Admin) {
            com.backend.globeonclick.entity.Admin admin = (com.backend.globeonclick.entity.Admin) userDetails;
            claims.put("userId", admin.getAdminId());
            claims.put("role", admin.getRole().name()); // Incluye el rol específico (ADMIN o AGENT)
        } else if (userDetails instanceof com.backend.globeonclick.entity.User) {
            com.backend.globeonclick.entity.User user = (com.backend.globeonclick.entity.User) userDetails;
            claims.put("userId", user.getUserId());
            claims.put("role", user.getRole().name()); // Siempre será USER
        }

        return generateToken(claims, userDetails);
    }

    private String getUserType(UserDetails userDetails) {
        if (userDetails instanceof com.backend.globeonclick.entity.Admin) {
            return "ADMIN"; // El tipo sigue siendo ADMIN independientemente del rol
        } else if (userDetails instanceof com.backend.globeonclick.entity.User) {
            return "USER";
        }
        return "UNKNOWN";
    }

    public String generateToken(
            Map<String, Object> extractClaims,
            UserDetails userDetails
    ) {
        String email = userDetails instanceof com.backend.globeonclick.entity.User ? ((com.backend.globeonclick.entity.User) userDetails).getAuthenticationUsername() : userDetails.getUsername();
        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractUserName(token);
        String userEmail = userDetails instanceof com.backend.globeonclick.entity.User ? ((com.backend.globeonclick.entity.User) userDetails).getAuthenticationUsername() : userDetails.getUsername();
        return (email.equals(userEmail)) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public<T> T extractClaim(String token, Function<Claims, T> claimsFunction) {
        final Claims claims  = extractAllClaims(token);
        return claimsFunction.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}