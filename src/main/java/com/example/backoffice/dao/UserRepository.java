package com.example.backoffice.dao;

import com.example.backoffice.entity.DonationCenter;
import com.example.backoffice.entity.Role;
import com.example.backoffice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
    long count();
    long countByRole(Role role);
    List<User> findByRole(Role role);

}
