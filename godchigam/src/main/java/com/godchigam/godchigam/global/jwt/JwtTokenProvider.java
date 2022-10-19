package com.godchigam.godchigam.global.jwt;

import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${secret.access}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }



    public String generateToken(String logindId){
        long tokenPeriod = 10000L * 60L * 40L * 10L;

        Claims claims = Jwts.claims().setSubject(String.valueOf(logindId));

        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims getClaimsToken(String token) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(token)
                .getBody();
    }

    //토큰 유효성 검사
    //TODO 예외 처리 ! 특히 refresh token이 만료 됐을 떄는 refresh token도 재발행 해줘야함.
    public boolean isValidToken(String token) {
        log.info("isValidToken is : " + token);
        try {
            Claims accessClaims = getClaimsToken(token);
            log.info("Access expireTime: " + accessClaims.getExpiration());
            log.info("Access loginId: " + accessClaims.getSubject());
            return true;
        }  catch (JwtException exception) {
            log.error("Token Tampered");
            throw new BaseException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (NullPointerException exception) {
            log.error("Token is null");
            throw new BaseException(ErrorCode.EMPTY_JWT);
        }
    }

    public String getAccessTokenHeader(HttpServletRequest request){
        return request.getHeader("Authorization");
    }

    //Request의 Header에서 token값 가져오기 "ACCESS_TOKEN" : "TOKEN값'
    public String resolveAccessToken(HttpServletRequest request){
        return request.getHeader("ACCESS_TOKEN");
    }

    //토큰에서 회원 정보 추출
    public String getUserLoginId(String accessToken) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody().getSubject();
    }

}

