package com.example.backoffice.service.impl;

import com.example.backoffice.dao.RequestRepository;
import com.example.backoffice.entity.Admin;
import com.example.backoffice.dao.AdminRepository;
import com.example.backoffice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final RequestRepository requestRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void save(Admin admin) {
        if (admin.getPassword() != null) {
            // Encoder le mot de passe
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        } else {
            // Si update et mot de passe null, garder l'ancien mot de passe (à gérer selon besoin)
            Admin existingAdmin = adminRepository.findById(admin.getId()).orElse(null);
            if (existingAdmin != null) {
                admin.setPassword(existingAdmin.getPassword());
            }
        }
        adminRepository.save(admin);
    }

    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    @Override
    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    @Override
    public Optional<Admin> findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Override
    public Admin update(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public void deleteById(Long id) {
        adminRepository.deleteById(id);
    }
    public long getTotalAdmins() {
        return adminRepository.count();

    }

}