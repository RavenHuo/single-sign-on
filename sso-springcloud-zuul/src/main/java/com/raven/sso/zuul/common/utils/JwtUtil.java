package com.raven.sso.zuul.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: huorw
 * @create: 2019-10-11 17:48
 */
@Slf4j
public class JwtUtil {

    private JwtUtil(){}

    /** token 过期时间单位为  天 */
    public static final int CALENDAR_DATE = Calendar.DATE;

    private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS256;

    private static final Map<String, Object> HEADER_PARAM_MAP = new HashMap<String, Object> ()
    {
        private static final long serialVersionUID = 7194412915733853882L;
        {
            put("typ", "JWT");

        }
    };


    public static String generateJwt(Map<String, Object> tokenMap, String secret) {
        Key signingKey = getJwtKey(secret);


        String jwtToken = Jwts.builder()
                .setHeaderParams(HEADER_PARAM_MAP)
                .addClaims(tokenMap)
                .signWith(ALGORITHM, signingKey).setExpiration(generateExpireTime(null)).compact();
        return jwtToken;
    }


    /**
     * 生成Token
     * @param tokenMap
     * @return
     */
    public static String generateJwtTokenByDays(Map<String, Object> tokenMap, Integer jwtInvalidDays, String secret){
        Key signingKey = getJwtKey(secret);


        String jwtToken = Jwts.builder()
                .setHeaderParams(HEADER_PARAM_MAP)
                .addClaims(tokenMap)
                .signWith(ALGORITHM, signingKey).setExpiration(generateExpireTime(jwtInvalidDays)).compact();
        return jwtToken;
    }

    private static Date generateExpireTime(Integer jwtInvalidDays) {
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        if(jwtInvalidDays != null && jwtInvalidDays > 0) {
            nowTime.add(CALENDAR_DATE, jwtInvalidDays);
        }
        Date expiresDate = nowTime.getTime();
        return expiresDate;
    }

    /**
     *   生成加密key
     * @param secret
     * @return
     */
    private static Key getJwtKey(String secret) {

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, ALGORITHM.getJcaName());
        return signingKey;
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public static Map<String, Object> parseToken(String token, String secret) {

        Claims claims = null;

        Key signingKey = getJwtKey(secret);

        try {
            //对token进行解码
            claims = Jwts.parser().setSigningKey(signingKey)
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            log.warn("token失效   token={},   e={}", token, e);
        } catch (Exception e) {
            log.warn("解析toke失败  token={},  e={}", token, e);
        }
        return claims;
    }


}
