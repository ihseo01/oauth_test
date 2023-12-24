package com.ina.oauth_test.common.config;

import com.ina.oauth_test.common.auth.CustomOAuth2UserService;
import com.ina.oauth_test.common.status.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .requestMatchers("/private/**").authenticated() //private로 시작하는 uri는 로그인 필수
                .anyRequest().permitAll() //나머지 uri는 모든 접근 허용
                .and().oauth2Login()
                .loginPage("/loginForm") //로그인이 필요한데 로그인을 하지 않았다면 이동할 uri 설정
                .defaultSuccessUrl("/") //OAuth 구글 로그인이 성공하면 이동할 uri 설정
                .userInfoEndpoint()//로그인 완료 후 회원 정보 받기
                .userService(customOAuth2UserService).and().and().build(); //로그인 후 받아온 유저 정보
    }
}
