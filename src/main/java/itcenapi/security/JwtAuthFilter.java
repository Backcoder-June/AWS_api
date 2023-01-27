package itcenapi.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 클라이언트가 보낸 Token ( header 에 담아 ) => 검사하는 필터
@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final TokenProvider provider; // 토큰 검증하고 subject 받아오는 메소드 사용하기 위해 가져옴
    @Autowired
    public JwtAuthFilter(TokenProvider provider) {
        this.provider = provider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Header 에서 Token 가져오기
        try {
            String token = parseBearerToken(request);

            log.info("JWT token filter is running... - token : {}", token);

            // 토큰 위조여부 검사
            if (token != null) {
                String userId = provider.validateAndGetUserId(token);// 여기 위조면 exception 이니까 try-catch
                log.info("인증된 tokensubject-userid : {}", userId);

                /** 이 부분에서, websecurityconfig 에서 authenticated 인증을 함 **/
                // 인증 완료!! api서버에서는 SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, // 컨트롤러의 @AuthenticationPrincipal 값
                        null,
                        AuthorityUtils.NO_AUTHORITIES
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                ((SecurityContext) securityContext).setAuthentication(authentication);

                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("인증되지 않은 사용자 입니다.");
        }

        // fileter-chain 에 내가 만든 custom-filter 를 실행시킴
        filterChain.doFilter(request, response);

    }

    private String parseBearerToken(HttpServletRequest request) {
        // 요청 헤더에서 Token 가져옴
        String bearerToken = request.getHeader("Authorization"); //'Authorization' : 'Bearer dsapf#Esdafs(token)';
        // 이 bearerToken 은 bearer 라는 String이 붙어있어서, ( 보낼때 그렇게 보내는게 규칙 ) => 이거 떼주는 작업 필요
        if (StringUtils.hasText(bearerToken)
                && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7); // Bearer 다음 index 부터 끝가지 = Token 자체
        }
        return null;
    }



}
