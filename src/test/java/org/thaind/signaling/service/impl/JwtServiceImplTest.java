package org.thaind.signaling.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.thaind.signaling.dto.internal.protocol.Response;
import org.thaind.signaling.service.JwtService;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Signature;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author duyenthaind
 */
class JwtServiceImplTest {

    @Test
    void testCreateJwt(){
        String secret = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTY0OTEyMjc2MiwiaWF0IjoxNjQ5MTIyNzYyfQ.nEsZzxLDXOYJQzbpyXemkqF2wONJSufoKkbl1BlKK0A";
        String userId = "user1";
        Key hmac = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        String token = Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 7*24*60*60*1000))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
        System.out.println(token);
        assertNotEquals("", token);
    }

    @Test
    void testParseClaim(){
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJ1c2VyMSIsImlhdCI6MTY0OTIwOTgyMSwiZXhwIjoxNjQ5ODE0NjIxfQ.kz_qGLK1Y1OF6sYTaqnsTxgx3Dv8yb2mCSOZZyR9RGA";
        JwtService jwt = new JwtServiceImpl();
        Response res = jwt.verifyAccessToken(accessToken);
        System.out.println(res.getRes() + "\t" + res.getMessage());
        Claims claims = jwt.parseAllClaims(accessToken);
        System.out.println(claims);
        System.out.println(claims.get("userId"));
        assertEquals("user1", claims.get("userId"));
    }
}