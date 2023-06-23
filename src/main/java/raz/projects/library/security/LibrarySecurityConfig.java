package raz.projects.library.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class LibrarySecurityConfig {

    private final JwtAuthenticationFilter filter;

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .addFilterBefore(filter, BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/online-library/login/**").permitAll();
                    auth.requestMatchers("/online-library/**").authenticated();
                })
                .httpBasic(basic -> basic.authenticationEntryPoint(new LibraryAuthenticationEntryPoint()))
                .build();
    }

}
