package com.zeffon.danzhu.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Create by Zeffon on 2020/10/1
 */
@Component
public class JwtToken {

    private static String jwtKey;
    private static Integer expiredTimeIn;
    private static Integer defaultScope = 8;

    @Value("${danzhu.security.jwt-key}")
    public void setJwtKey(String jwtKey) {
        JwtToken.jwtKey = jwtKey;
    }

    @Value("${danzhu.security.token-expired-in}")
    public void setExpiredTimeIn(Integer expiredTimeIn) {
        JwtToken.expiredTimeIn = expiredTimeIn;
    }

    // 获取令牌的数据
    public static Optional<Map<String, Claim>> getClaims(String token) {
        DecodedJWT decodedJWT;
        Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            decodedJWT = jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
        return Optional.of(decodedJWT.getClaims());
    }

    // 校验令牌
    public static Boolean verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static Boolean verifyToken2(String bearerToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtKey);
            String[] tokens = bearerToken.split(" ");
            String token = tokens[1];
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String makeToken(Integer uid, Integer scope) {
        return JwtToken.getToken(uid, scope);
    }

    public static String makeToken(Integer uid) {
        return JwtToken.getToken(uid, JwtToken.defaultScope);
    }

    // 生成令牌
    private static String getToken(Integer uid, Integer scope) {
        // jjwt  auth0
        // 1. 选择算法(传入一个随机字符串.就是盐)
        Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtKey);

        // 2. 计算过期时间
        Map<String, Date> map = JwtToken.calculateExpiredIssues();

        // 3. 生成令牌
        return JWT.create()
                .withClaim("uid", uid)
                .withClaim("scope", scope)
                .withExpiresAt(map.get("expiredTime")) // 在哪个时间点过期 需要自己计算过期时间
                .withIssuedAt(map.get("now")) // 签发时间
                .sign(algorithm);
    }

    // 计算过期时间(现在-有效时长)
    private static Map<String, Date> calculateExpiredIssues() {
        Map<String, Date> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.SECOND, JwtToken.expiredTimeIn);
        map.put("now", now);
        map.put("expiredTime", calendar.getTime());
        return map;
    }
}
