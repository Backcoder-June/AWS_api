package itcenapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // API CORS 정책 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("api/**")  //api url에 있어서
                .allowedOrigins("http://localhost:3000",
                        "http://todoappbucket-001.s3-website.ap-northeast-2.amazonaws.com") //Cross Origin 주소 해당 주소만 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 메소드 허용
                .allowedHeaders("*")
                .allowCredentials(true) // 토큰 사용 true
                .maxAge(3600); //3600초 대기

//        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}
