package com.example.backoffice.controller;

import com.example.backoffice.entity.Donation;
import com.example.backoffice.entity.Request;
import com.example.backoffice.entity.User;
import com.example.backoffice.dao.RequestRepository;
import com.example.backoffice.dao.UserRepository;
import com.example.backoffice.dao.DonationCenterRepository;
import com.example.backoffice.service.DonationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/donations")
public class DonationController {

    private final DonationService donationService;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final DonationCenterRepository donationCenterRepository;

    @Autowired
    public DonationController(
            DonationService donationService,
            UserRepository userRepository,
            RequestRepository requestRepository,
            DonationCenterRepository donationCenterRepository
    ) {
        this.donationService = donationService;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.donationCenterRepository = donationCenterRepository;
    }
    @GetMapping
    public String listDonations(Model model) {
        List<Donation> donations = donationService.findAllDonations();
        donations.sort(Comparator.comparingInt(donation -> donation.getId().intValue()));
// Récupère la liste des dons
        model.addAttribute("donations", donations);
        return "donations/donation-list"; // Affiche la liste des dons
    }
    @GetMapping("/create/{requestId}")
    public String showMedicalForm(@PathVariable Long requestId, Model model) {
        model.addAttribute("requestId", requestId);
        return "donations/medical-form";
    }

    @PostMapping("/validate/{requestId}")
    public String validateMedicalForm(@PathVariable Long requestId,
                                      @RequestParam boolean canDonate,
                                      Model model) {
        if (canDonate) {
            model.addAttribute("requestId", requestId);
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("centers", donationCenterRepository.findAll());
            model.addAttribute("donation", new Donation());
            return "donations/donation-form";
        } else {
            return "redirect:/requests"; // ou page de rejet
        }
    }

    @PostMapping("/save")
    public String saveDonation(@Valid @ModelAttribute("donation") Donation donation,
                               @RequestParam("userEmail") String userEmail,
                               @RequestParam("requestId") Long requestId,
                               Model model) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            model.addAttribute("error", "Utilisateur introuvable !");
            return "donations/donation-form"; }
            if (donation.getRequest() != null && donation.getRequest().getRequiredBloodUnits() <= 0) {
                model.addAttribute("error", "Cette requête est déjà satisfaite.");
                return "error-page";

        }

        Request request = requestRepository.findById(requestId).orElse(null);

        donation.setUser(user);
        donation.setRequest(request);
        donation.setDate(LocalDateTime.now());
        donation.setValidated(true);

        donationService.saveDonation(donation);
        return "redirect:/requests"; // ou /donations
    }


    // Afficher les détails d'un don
    @GetMapping("/details/{donationId}")
    public String showDonationDetails(@PathVariable Long donationId, Model model) {
        Donation donation = donationService.findDonationById(donationId);
        if (donation != null) {
            model.addAttribute("donation", donation);
            model.addAttribute("request", donation.getRequest());
            return "donations/donation-details"; // Vue qui affichera les détails
        } else {
            return "redirect:/donations/list"; // Redirige vers la liste des dons si non trouvé
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteDonation(@PathVariable("id") Long id) {
        // Appel du service pour supprimer le don
        donationService.deleteDonation(id);

        // Rediriger vers la page de liste des dons après suppression
        return "redirect:/donations";
    }


}
