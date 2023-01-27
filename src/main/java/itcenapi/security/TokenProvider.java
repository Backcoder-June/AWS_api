package itcenapi.security;

// 토큰을 발급하고, 서명위조를 확인해주는 객체

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import itcenapi.userapi.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Slf4j
public class TokenProvider {

    // 토큰 서명에 사용할 불변성의 비밀 키 (512 바이트 이상 랜덤문자열) 아무거나 때려 박고
    private static final String SECRET_KEY = "Q4NSl604sgyHJj1qwEkR3xcUeR4uUAt7WJraD7EN3O9DVM4xxYuHxMEbSF4XXyYJkal13eqgB0F7Bq4H";

    // 토큰 발급 메소드
    public String createToken(UserEntity userEntity) {
        // 만료시간 설정 ( 사이트 정책에 따라 다름 )
        Date expireDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS) //하루 ChronoUnit 에서 한달 하루 1년 이런식으로 설정
        );

        // 토큰 생성
        return Jwts.builder()
                //header 에 들어갈 서명
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())  // 싸인
                ,SignatureAlgorithm.HS512 //알고리즘화
                )
                .setSubject(userEntity.getId())  //subject : 토큰 식별자 => 값으로 보통 엔티티 pk 값
                .setIssuer("todo app") //iss : 발급자 정보
                .setIssuedAt(new Date()) //iat : 토큰발급 시간
                .setExpiration(expireDate) //exp : 토큰 만료 시간
                .compact(); // 묶기
    }

    // 클라이언트가 보낸 토큰을 decoding, parsing 해서 토큰 위조여부 검증 (JWT 라이브러리 제공)
    // @param token => 클라이언트가 전송한 인코딩된 토큰
    // @return 값 : 토큰에서 해당 subject( 식별자 - pk 정도 ) 반환 => 이 Token 을 가진 회원이 누군지 알려줄 값

    public String validateAndGetUserId(String token) {  //인코딩된 token 을 클라에서 받아와서

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())) // 토큰 발급 당시의 signature 넣어줌
                .build()
                // 발급당시 서명과, 클라에서 받아온 token 의 서명을 비교해야 한다.
                // => 위조되지 않았다면 body에 페이로드(claims)를 리턴
                // => 위조되었다면 Exception
                // token 디코딩 서명기록 파싱
                .parseClaimsJwt(token)
                .getBody(); // => body 안에 Token 정보들 (subject ~ ) 다 담김

        return claims.getSubject();  // subject => 토큰을 발급받은 유저의 식별자(pk)
    }








    //
}
