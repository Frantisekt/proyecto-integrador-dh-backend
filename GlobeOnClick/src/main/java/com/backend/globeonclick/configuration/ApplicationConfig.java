package com.backend.globeonclick.configuration;

import com.backend.globeonclick.repository.IUserRepository;
import com.backend.globeonclick.repository.IAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final IUserRepository userRepository;
    private final IAdminRepository adminRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // Primero buscar en administradores
            var adminOptional = adminRepository.findByEmail(username);
            if (adminOptional.isPresent()) {
                return adminOptional.get();
            }

            // Si no es admin, buscar en usuarios
            var userOptional = userRepository.findByEmail(username);
            if (userOptional.isPresent()) {
                return userOptional.get();
            }

            throw new UsernameNotFoundException("Usuario no encontrado");
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

