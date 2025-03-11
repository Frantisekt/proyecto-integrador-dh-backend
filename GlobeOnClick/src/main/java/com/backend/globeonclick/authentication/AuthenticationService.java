package com.backend.globeonclick.authentication;


import com.backend.globeonclick.authentication.request.AuthenticationRequest;
import com.backend.globeonclick.authentication.request.RegisterRequest;
import com.backend.globeonclick.authentication.response.AuthenticationResponse;
import com.backend.globeonclick.configuration.jwt.JwtService;
import com.backend.globeonclick.entity.Admin;
import com.backend.globeonclick.entity.Role;
import com.backend.globeonclick.entity.User;
import com.backend.globeonclick.repository.IAdminRepository;
import com.backend.globeonclick.repository.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
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
            PasswordEncoder passwordEncoder,
            IAdminRepository adminRepository,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .name(request.getName())
                .paternalSurname(request.getPaternalSurname())
                .maternalSurname(request.getMaternalSurname())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .dni(request.getDni())
                .newsletter(request.getNewsletter())
                .state(true)
                .role(Role.USER) // Asignar rol USER por defecto
                .build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .type("USER")
                .role(user.getRole())
                .build();
    }

    // Método para registrar administradores (nuevo)
    public AuthenticationResponse registerAdmin(RegisterRequest request, Role role) {
        // Validar que el rol sea válido para un Admin
        if (role != Role.ADMIN && role != Role.AGENT) {
            throw new IllegalArgumentException("El rol debe ser ADMIN o AGENT para administradores");
        }

        var admin = Admin.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .state(true)
                .role(role) // Permitir asignar ADMIN o AGENT
                .build();
        adminRepository.save(admin);
        var jwt = jwtService.generateToken(admin);
        return AuthenticationResponse.builder()
                .token(jwt)
                .userId(admin.getAdminId())
                .email(admin.getEmail())
                .name(admin.getName())
                .type("ADMIN") // Se mantiene el tipo como ADMIN para ambos roles
                .role(admin.getRole()) // Pero el rol puede ser ADMIN o AGENT
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            log.info("Iniciando autenticación para: {}", request.getEmail());

            // Autenticar con Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            log.info("Usuario autenticado: {}", userDetails.getUsername());

            // Generar token JWT
            String jwt = jwtService.generateToken(userDetails);

            // Construir respuesta según el tipo de usuario
            AuthenticationResponse.AuthenticationResponseBuilder responseBuilder = AuthenticationResponse.builder()
                    .token(jwt)
                    .email(userDetails.getUsername());

            if (userDetails instanceof User) {
                User user = (User) userDetails;
                return responseBuilder
                        .userId(user.getUserId())
                        .name(user.getName())
                        .type("USER")
                        .role(user.getRole())
                        .build();
            } else if (userDetails instanceof Admin) {
                Admin admin = (Admin) userDetails;
                return responseBuilder
                        .userId(admin.getAdminId())
                        .name(admin.getName())
                        .type("ADMIN") // Tipo sigue siendo ADMIN
                        .role(admin.getRole()) // Pero el rol puede ser ADMIN o AGENT
                        .build();
            }

            return responseBuilder.build();

        } catch (Exception e) {
            log.error("Error en autenticación: {}", e.getMessage());
            return AuthenticationResponse.builder()
                    .error("Error de autenticación: " + e.getMessage())
                    .build();
        }
    }
}