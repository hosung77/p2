package com.github.project2.config.security;


import io.jsonwebtoken.Claims;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

//    private final Set<String> blackList = new HashSet<>();

    @Value("${jwt.secret-key-source}")
    private String secretKeySource;

    private String secretKey;

    @PostConstruct
    public void setUp() {
        secretKey = Base64.getEncoder()
                .encodeToString(secretKeySource.getBytes());
    }

    private long tokenValidMillisecond = 1000L * 60 * 60;

    private final UserDetailsService userDetailsService;

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-Auth-Token");
    }


        // JWT 토큰 생성
        public String createToken(String email, List<String> roles ) {
        Claims claims = Jwts.claims().setSubject(email);

        claims.put("roles", roles);
        Date now = new Date();
        return  Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        }

    public boolean validateToken(String jwtToken) {
        try{
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken).getBody();
            Date now = new Date();
            return  !claims.getExpiration().after(now);
        } catch (Exception e) {
            return false;
        }
    }


    public Authentication getAuthentication(String jwtToken) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserEmail(jwtToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUserEmail(String jwtToken) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken).getBody().getSubject();
    }

//
//    // 로그아웃 처리: blackList에 토큰을 추가
//    public void blacklistToken(String token) {
//        blackList.add(token);
//    }
//
//    public boolean isTokenBlacklisted(String token) {
//        return blackList.contains(token);
//    }
}
