package br.com.evandro.todoList.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig{

    @Autowired
    SecurityFilter securityFilter;

    private static final String requestSwagger[] = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**"
    };

    private static final String requestAuthUser[] = {
            "/api/auth/reset-password",
            "/api/auth/forgot-password",
            "/api/auth/refresh-token",
            "/send-email/reset-password",
            "/api/auth/sign-in"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests( auth -> {
                    auth.requestMatchers("/api/users").permitAll()
                            .requestMatchers("/api/users/generate-username").permitAll()
                            .requestMatchers(requestAuthUser).permitAll()
                            .requestMatchers(requestSwagger).permitAll();
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, BasicAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
