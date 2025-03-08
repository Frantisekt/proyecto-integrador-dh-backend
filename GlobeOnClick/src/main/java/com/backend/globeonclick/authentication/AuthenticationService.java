package com.backend.globeonclick.authentication;


import com.backend.globeonclick.authentication.request.AuthenticationRequest;
import com.backend.globeonclick.authentication.request.RegisterRequest;
import com.backend.globeonclick.authentication.response.AuthenticationResponse;
import com.backend.globeonclick.configuration.jwt.JwtService;
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
                .build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        try {
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
                    .email(user.getEmail())
                    .name(user.getName())
                    .type("USER")
                    .build();
        } catch (Exception e) {
            log.error("Error en login de usuario: {}", e.getMessage());
            return AuthenticationResponse.builder()
                    .error("Credenciales inválidas")
                    .build();
        }
    }

    public AuthenticationResponse adminLogin(AuthenticationRequest request) {
        try {
            log.info("Iniciando proceso de login para: {}", request.getEmail());
            
            // Buscar admin
            var admin = adminRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Administrador no encontrado"));
            
            log.info("Admin encontrado con ID: {}", admin.getAdminId());
            log.info("Rol del admin: {}", admin.getRole());
            
            // Verificar rol
            if (admin.getRole() != Role.ADMIN) {
                log.error("Usuario no tiene rol de admin: {}", admin.getRole());
                throw new AccessDeniedException("No tiene permisos de administrador");
            }

            // Verificar contraseña
            log.info("Verificando credenciales para: {}", admin.getEmail());
            log.info("Contraseña almacenada (hash): {}", admin.getPassword());
            
            try {
                Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                    )
                );
                log.info("Autenticación exitosa para: {}", authentication.getName());
            } catch (BadCredentialsException e) {
                log.error("Error en credenciales para {}: {}", request.getEmail(), e.getMessage());
                return AuthenticationResponse.builder()
                        .error("Contraseña incorrecta")
                        .build();
            }

            // Generar token
            var jwt = jwtService.generateAdminToken(admin);
            log.info("Token generado exitosamente para: {}", admin.getEmail());

            return AuthenticationResponse.builder()
                    .token(jwt)
                    .email(admin.getEmail())
                    .name(admin.getName())
                    .adminId(admin.getAdminId())
                    .type("ADMIN")
                    .role(admin.getRole())
                    .build();

        } catch (Exception e) {
            log.error("Error en proceso de login: ", e);
            return AuthenticationResponse.builder()
                    .error("Error en la autenticación: " + e.getMessage())
                    .build();
        }
    }


}
