package com.backend.globeonclick;


import com.backend.globeonclick.entity.Admin;
import com.backend.globeonclick.entity.Role;
import com.backend.globeonclick.repository.IAdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class GlobeOnClickApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlobeOnClickApplication.class, args);
    }

    @Bean
    CommandLineRunner init(IAdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("Verificando si existe el admin...");
            if (adminRepository.findByEmail("admin@globeonclick.com").isEmpty()) {
                System.out.println("Creando admin por defecto...");
                Admin admin = Admin.builder()
                        .name("Administrador")
                        .username("admin")
                        .email("admin@globeonclick.com")
                        .password(passwordEncoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .state(true)
                        .type("ADMIN")
                        .build();

                Admin savedAdmin = adminRepository.save(admin);
                System.out.println("Admin creado con ID: " + savedAdmin.getAdminId());
            } else {
                System.out.println("El admin ya existe en la base de datos");
            }
        };
    }
}
