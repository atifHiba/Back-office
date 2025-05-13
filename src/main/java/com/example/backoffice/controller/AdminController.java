package com.example.backoffice.controller;

import com.example.backoffice.dao.DonationCenterRepository;
import com.example.backoffice.dao.DonationRepository;
import com.example.backoffice.dao.RequestRepository;
import com.example.backoffice.entity.Admin;
import com.example.backoffice.service.AdminService;
import com.example.backoffice.service.DonationCenterService;
import com.example.backoffice.service.DonationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller

@RequiredArgsConstructor
public class AdminController {
    private final DonationService donationService;
    private final AdminService adminService;
    private final DonationCenterService donationCenterService;
    private final RequestRepository requestRepository;
    private final DonationRepository donationRepository;

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        Pageable pageable = PageRequest.of(0, 1); // Limite à 1 résultat
        List<Object[]> topCenter = donationRepository.findTopDonationCenters(pageable);
        List<Object[]>Center = donationRepository.findDonationCenters();
            long totalDonors = donationService.getTotalDonors();
            long totalDonations = donationService.getTotalDonations();
        long saturatedRequests = requestRepository.countSaturatedRequests();

        List<String> centerNames = new ArrayList<>();
        List<Long> donationCounts = new ArrayList<>();

        for (Object[] obj : Center) {
            centerNames.add((String) obj[0]);
            donationCounts.add((Long) obj[1]);
        }

        model.addAttribute("centerNames", centerNames);
        model.addAttribute("donationCounts", donationCounts);
        model.addAttribute("saturatedRequests", saturatedRequests);
            model.addAttribute("topCenter", topCenter);
            model.addAttribute("totalDonors", totalDonors);
            model.addAttribute("totalDonations", totalDonations);
        List<Object[]> requestStats = requestRepository.countRequestSatisfaction();

        List<String> statuses = new ArrayList<>();
        List<Long> statusCounts = new ArrayList<>();

        for (Object[] obj : requestStats) {
            statuses.add((String) obj[0]);
            statusCounts.add((Long) obj[1]);
        }

        model.addAttribute("statuses", statuses);
        model.addAttribute("statusCounts", statusCounts);
       return "admin/dashboard";
      // correspond à dashboard.html dans templates
    }
    // Afficher la liste des admins
    @GetMapping("/admin/list")
    public String listAdmins(org.springframework.ui.Model model) {
        model.addAttribute("admins", adminService.findAll());
        return "admin/admin-list";
    }



    // Afficher le formulaire de création
    @GetMapping("/admin/form")
    public String showCreateForm(@RequestParam(required = false) Long id, org.springframework.ui.Model model) {
        Admin admin = (id != null) ? adminService.findById(id).orElse(new Admin()) : new Admin();
        model.addAttribute("admin", admin);
        return "admin/admin-form";
    }

    // Sauvegarder un admin (ajout ou update)
    @PostMapping("/admin/save")
    public String saveAdmin(@ModelAttribute Admin admin) {
        adminService.save(admin);
        return "redirect:/admin/list";
    }

    // Supprimer un admin
    @GetMapping("/admin/delete")
    public String deleteAdmin(@RequestParam Long id) {
        adminService.deleteById(id);
        return "redirect:/admin/list";
    }

    @GetMapping("/admin/centres")
    public String showCentres() {
        return "redirect:/donationCenters";  // Rediriger vers le contrôleur des centres de dons
    }

    @GetMapping("/admin/demandes")
    public String showDemandes() {
        return "redirect:/requests";  // Vue demandes.html
    }

    @GetMapping("/admin/dons")
    public String showDons() {
        return "redirect:/donations";  // Vue dons.html
    }


    @GetMapping("/admin/utilisateurs")
    public String showUtilisateurs() {
        return "redirect:/users"; // Vue utilisateurs.html
    }

    @GetMapping("/rapports")
    public String showRapports() {
        return "admin/rapports";  // Vue rapports.html
    }

    @GetMapping("/logout")
    public String logout() {
        // Logique de déconnexion
        return "redirect:/login";  // Redirection vers la page de connexion
    }
}
