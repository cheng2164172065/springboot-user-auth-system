package com.example.demo.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 密钥，用来给token签名防伪造，随便一段长字符串
    private final Key key = Keys.hmacShaKeyFor(
            "this-is-a-very-secret-key-123456789012".getBytes()
    );

    // token有效期：7天
    private final long EXPIRATION = 1000 * 60 * 60 * 24 * 7;

    // 生成token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 从token里解析用户名
    public String parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
