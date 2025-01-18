package com.baekyaton.backend.global.jwt;

import com.baekyaton.backend.domain.user.entity.User;
import com.baekyaton.backend.global.oauth2.CustomOAuth2UserService;
import com.baekyaton.backend.global.oauth2.dto.CustomOAuth2User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    private final JwtProperties jwtProperties;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final CustomOAuth2UserService customOAuth2UserService;

    private Key createSecretKey() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtProperties.getSecretKey());
        return new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    }

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(String.valueOf(user.getId()))
                .signWith(createSecretKey(), signatureAlgorithm)
                .compact();
    }

    public boolean validToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(createSecretKey())
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Long memberId = Long.valueOf(claims.getSubject());
        UserDetails userDetails = new CustomOAuth2User(memberId);

        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_USER")
        );

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                token,
                authorities
        );
    }

//    public Long getMemberId(String token) {
//        Claims claims = getClaims(token);
//        return Long.valueOf(claims.getSubject());
//    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(createSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
