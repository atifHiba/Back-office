package com.example.backoffice.controller;

import com.example.backoffice.dao.AdminRepository;
import com.example.backoffice.dao.UserRepository;
import com.example.backoffice.entity.Admin;
import com.example.backoffice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "auth/login"; // correspond à login.html dans templates
    }
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new Admin()); // modèle vide pour le formulaire
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("admin") Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
        return "redirect:/login";
    }


    @Autowired
    private AdminRepository adminRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

}

