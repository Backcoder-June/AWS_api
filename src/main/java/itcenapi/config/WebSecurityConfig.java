package itcenapi.config;

import itcenapi.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

// 스프링 설정 파일
// @Configuration

// Configuration 포함 추가적인 설정
@EnableWebSecurity
public class WebSecurityConfig {
    // 패스워드 인코딩 클래스 등록 ( Bean 등록 )
    @Bean //외부 객체를 가져와서 Bean 등록 해줄 때는 @Bean 사용 <bean id=? class=? />  id : encoder class : PasswordEncoder
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    public WebSecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    //시큐리티 설정 (Bean FilterChain 방식 - 최근)

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //시큐리티 빌더
        http.cors() //cross origin 정책
                .and()
                .csrf() //CSRF 정책
                .disable() // Security 가 기본 제공하는 인증에서 cors, csrf 사용안하고, 내가 만든걸로 따로 적용하겠다.
                .httpBasic().disable() // 기본 security 로그인 인증 해제 ( Token 인증으로 대체 할 것임 )

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 관리 - 세션 만드는 정책을 stateless 하게 만들겠다 => 즉, 세션인증을 안쓰겠다. (Token 대체)
                .and()
                .authorizeRequests().antMatchers("/","/api/auth/**", "api/posts/allboard").permitAll() // 인증 요청 중, /, /api/auth 경로는 인증하지 않고 모두 허용 ( 즉 인증이 필요없는 페이지에 대해 접근 허용 )
                // 여기에 로그인 없이 접근 가능한 전체게시물 조회, 상세 게시물 조회 등은 풀어놓고 사용 ( 나머지 수정, 삭제 등은 authentication 되도록 )
                .anyRequest().authenticated(); // 모든 요청에대해 authentication 필요 ( 위의 예외 처리 제외하고 )
//                .anyRequest().permitAll();


    // 토큰 인증 필터 등록
        http.addFilterAfter(
                jwtAuthFilter, // 커스텀필터 (만들어 둔거)
                CorsFilter.class //import 주의 Spring 꺼 사용
        );




        return http.build();
    }



}
