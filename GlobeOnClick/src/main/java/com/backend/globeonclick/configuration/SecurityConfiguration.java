package com.backend.globeonclick.configuration;

import com.backend.globeonclick.configuration.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                    //.requestMatchers("/auth/**").permitAll()
                    //.requestMatchers("/users/me").authenticated()
                    //.requestMatchers("/", "/index.html", "/login.html", "/register.html").permitAll()
                    //.requestMatchers("/css/**", "/js/**").permitAll()
                    //.requestMatchers("/users/**").hasRole("ADMIN")
                    .anyRequest().permitAll()
                    //.anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                /*.oauth2ResourceServer(spec -> spec.jwt(Customizer.withDefaults()))*/
                /*.httpBasic(Customizer.withDefaults())*/
                .build();
    }
}