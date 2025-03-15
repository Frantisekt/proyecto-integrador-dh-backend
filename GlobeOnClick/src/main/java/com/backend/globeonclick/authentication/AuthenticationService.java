package com.backend.globeonclick.authentication;

import com.backend.globeonclick.authentication.request.AuthenticationRequest;
import com.backend.globeonclick.authentication.request.RegisterRequest;
import com.backend.globeonclick.authentication.response.AuthenticationResponse;
import com.backend.globeonclick.configuration.jwt.JwtService;
import com.backend.globeonclick.entity.User;
import com.backend.globeonclick.entity.Admin;
import com.backend.globeonclick.entity.Role;
import com.backend.globeonclick.repository.IUserRepository;
import com.backend.globeonclick.repository.IAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final IUserRepository userRepository;
    private final IAdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(
            IUserRepository userRepository,
            IAdminRepository adminRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        try {
            // Validar si el email ya existe
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("El email ya está registrado");
            }

            // Validar si el DNI ya existe
            if (userRepository.existsByDni(request.getDni())) {
                throw new RuntimeException("El DNI ya está registrado");
            }

            var user = User.builder()
                    .name(request.getName())
                    .paternalSurname(request.getPaternalSurname())
                    .maternalSurname(request.getMaternalSurname())
                    .username(request.getUsername())
                    .email(request.getEmail().toLowerCase().trim()) // Normalizar email
                    .password(passwordEncoder.encode(request.getPassword()))
                    .dni(request.getDni().trim()) // Normalizar DNI
                    .newsletter(request.getNewsletter())
                    .role(Role.USER)
                    .state(true)
                    .build();

            user = userRepository.save(user);
            var jwt = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(jwt)
                    .type("USER")
                    .role("USER")
                    .userId(user.getUserId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RuntimeException("Error al registrar usuario: " + e.getMessage());
        }
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .type("USER")
                .role(user.getRole() != null ? user.getRole().name() : null)
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public AuthenticationResponse adminLogin(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Administrador no encontrado"));

        if (!admin.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("No tienes permisos de administrador");
        }

        var jwt = jwtService.generateToken(admin);
        return AuthenticationResponse.builder()
                .token(jwt)
                .type("ADMIN")
                .role(admin.getRole().name())
                .adminId(admin.getAdminId())
                .username(admin.getUsername())
                .email(admin.getEmail())
                .name(admin.getName())
                .build();
    }
}

