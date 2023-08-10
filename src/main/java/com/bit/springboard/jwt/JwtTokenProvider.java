package com.bit.springboard.jwt;

import com.bit.springboard.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtTokenProvider {
    //JWT Token의 signature의 유효성 검사에 사용될 키 값
    //BASE64 인코딩된 값
    //navercloud5todobootappdoohabitcamp702
    private static final String SECRET_KEY = "bmF2ZXJjbG91ZDV0b2RvYm9vdGFwcGRvb2hhYml0Y2FtcDcwMg==";
    //SECRET_KEY를 key 객체로 변환
    Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    /*
    * 사용자 정보를 받아서 JWT Token을 생성해주는 메소드
    * JSON 문자열을 Base64 인코딩하고 뒷부분에 서버에 있는 시그니처(SECRET_KEY)를 Hashing해서 추가
    * */

    public String create(User user) {
        //토큰 만료일 설정. 현재로부터 1일뒤로 설정
        Date expireDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        //JWT Token 생성하여 반환
        return Jwts.builder()
                //시그니처(서명)부분에 들어갈 key값 지정
                .signWith(key, SignatureAlgorithm.HS256)
                //페이로드에 들어갈 내용
                //토큰의 주인(sub)
                .setSubject(user.getUserId())
                //토큰 발행주체(iss) 임의로 지정
                .setIssuer("todo boot app")
                //토큰 발행일자(isa)
                .setIssuedAt(new Date())
                //토큰 만료일자
                .setExpiration(expireDate)
                //토큰발행
                .compact();

    }

    //JWT Token의 유효성 확인하는 메소드
    //subject에 담겨있는 username을 리턴한다.
    public String validateAndGetUsername(String token) {
        /*
        * 받아온 토큰 값을 파싱해서 유효성 검사
        * 토큰에 있는 시그니처와 서버에서 가지고 있는 시그니처값 비교
        * 시그니처 비교해서 일치하면 Claims 객체를 리턴
        * */
        Claims claims = Jwts.parserBuilder()
                            .setSigningKey(SECRET_KEY.getBytes())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
        //subject에 담겨있는 username을 리턴
        return claims.getSubject();
    }
}
