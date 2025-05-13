package com.example.backoffice.controller;

import com.example.backoffice.entity.DonationCenter;
import com.example.backoffice.service.CityService;
import com.example.backoffice.service.DonationCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/donationCenters")
public class DonationCenterController {
    private final CityService cityService;  // Déclaration de cityService

    private final DonationCenterService donationCenterService;

    @Autowired
    public DonationCenterController(CityService cityService, DonationCenterService donationCenterService) {
        this.cityService = cityService;
        this.donationCenterService = donationCenterService;
    }

    // Page de liste des centres de dons
    @GetMapping
    public String getAllDonationCenters(Model model) {
        List<DonationCenter> donationCenters = donationCenterService.getAllDonationCenters();
        model.addAttribute("donationCenters", donationCenters);
        return "donationCenter/donation_centers_list"; // Nom de la page de liste
    }

    // Formulaire de création d'un nouveau centre de don
    @GetMapping("/create")
    public String createDonationCenter(Model model) {
        model.addAttribute("donationCenter", new DonationCenter());

        model.addAttribute("cities", cityService.getAllCities());// Création d'un nouvel objet vide pour la création
        return "donationCenter/donation_center_form"; // Nom de la page de formulaire
    }

    // Formulaire pour éditer un centre de don existant
    @GetMapping("/edit/{id}")
    public String editDonationCenter(@PathVariable Long id, Model model) {
        Optional<DonationCenter> donationCenter = donationCenterService.getDonationCenterById(id);
        if (donationCenter.isPresent()) {
            model.addAttribute("donationCenter", donationCenter.get());
            return "donationCenter/donation_center_form"; // Nom de la page de formulaire
        }
        return "redirect:/donationCenters"; // Rediriger si le centre n'existe pas
    }

    // Sauvegarder le centre de don (création ou mise à jour)
    @PostMapping("/create")
    public String saveDonationCenter(@ModelAttribute DonationCenter donationCenter) {
        donationCenterService.createDonationCenter(donationCenter);
        return "redirect:/donationCenters"; // Redirection vers la liste après la création
    }

    // Mise à jour d'un centre de don
    @PostMapping("/edit/{id}")
    public String updateDonationCenter(@PathVariable Long id, @ModelAttribute DonationCenter donationCenter) {
        donationCenterService.updateDonationCenter(id, donationCenter);

        return "redirect:/donationCenters"; // Redirection vers la liste après la mise à jour
    }

    // Suppression d'un centre de don
    @GetMapping("/delete/{id}")
    public String deleteDonationCenter(@PathVariable Long id) {
        donationCenterService.deleteDonationCenter(id);
        return "redirect:/donationCenters"; // Redirection vers la liste après la suppression
    }




}
