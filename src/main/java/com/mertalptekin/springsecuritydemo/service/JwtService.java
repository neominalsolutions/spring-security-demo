package com.mertalptekin.springsecuritydemo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

// JwtService sınıfı, JWT oluşturma işlemlerini yönetir.
@Service
public class JwtService {

    // JWT oluşturmak için kullanılan gizli anahtar.256 bit değerin değerinde üretilmelelidir.
    private static final String SECRET = "wrD8PwDEtnDhlFoVyQ7q3Up2/aE3RIcIBZnzNFvUwK4=";

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 saat geçerli olacak
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSigningKey() {
        // Burada gerçek bir anahtar kullanmalısınız. Bu örnek için basit bir string kullanıldı.
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Token'ı çözümlemek için kullanılır.
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Token'ın geçerliliğini kontrol eder.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        Date tokenExpiration = parseToken(token).getExpiration();

        final String username = parseToken(token).getSubject();
        return (username.equals(userDetails.getUsername()) && !tokenExpiration.before(new Date()));
    }
}
