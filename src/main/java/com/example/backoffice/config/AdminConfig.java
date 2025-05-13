package com.example.backoffice.config;

import com.example.backoffice.entity.Admin;
import com.example.backoffice.dao.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminConfig{

    @Bean
    public CommandLineRunner initAdmin(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (adminRepository.findByUsername("salima").isEmpty()) {
                Admin admin = new Admin();
                admin.setUsername("salima");
                admin.setPassword(passwordEncoder.encode("salima")); // Mot de passe encodé

                adminRepository.save(admin);
                System.out.println("Admin ajouté avec succès.");
            }
        };
    }
}
