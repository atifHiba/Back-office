package com.example.backoffice.service;

import com.example.backoffice.entity.Admin;
import java.util.List;
import java.util.Optional;

public interface AdminService {
    Admin save(Admin admin);
    List<Admin> findAll();
    Optional<Admin> findById(Long id);
    Optional<Admin> findByUsername(String username);
    Admin update(Admin admin);
    void deleteById(Long id);
    long getTotalAdmins();
}