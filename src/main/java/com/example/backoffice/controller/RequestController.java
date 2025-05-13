package com.example.backoffice.controller;

import com.example.backoffice.dao.CityRepository;
import com.example.backoffice.dao.DonationCenterRepository;
import com.example.backoffice.dao.RequestRepository;
import com.example.backoffice.dao.UserRepository;
import com.example.backoffice.entity.*;
import com.example.backoffice.service.CityService;
import com.example.backoffice.service.DonationCenterService;
import com.example.backoffice.service.RequestService;
import com.example.backoffice.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;
    private final CityService cityService;
    private final DonationCenterService donationCenterService;
    private final UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DonationCenterRepository donationCenterRepository;
    @Autowired
    private RequestRepository requestRepository;
    @GetMapping
    public String listRequests(Model model) {
        model.addAttribute("requests", requestService.getAllRequests());
        return "request/request-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("request", new Request());
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("donationCenters", donationCenterService.getAllDonationCenters());
        model.addAttribute("urgencyLevels", UrgencyLevel.values());
        model.addAttribute("users", userService.getAllUsers());

        return "request/request-form";
    }

    @PostMapping("/create")
    public String createRequest(@ModelAttribute Request request,
                                @RequestParam("userEmail") String userEmail,
                                @RequestParam("cityId") Long cityId,
                                @RequestParam("donationCenterId") Long donationCenterId) {

        User user = userRepository.findByEmail(userEmail);
        if (user == null) throw new RuntimeException("Utilisateur introuvable.");

        City city = cityRepository.findById(cityId).orElseThrow(() -> new RuntimeException("Ville introuvable."));
        DonationCenter center = donationCenterRepository.findById(donationCenterId)
                .orElseThrow(() -> new RuntimeException("Centre de don introuvable."));

        request.setUser(user);
        request.setCity(city);
        request.setDonationCenter(center);

        requestRepository.save(request);
        return "redirect:/requests";
    }



    @GetMapping("/details/{id}")
    public String showRequestDetails(@PathVariable Long id, Model model) {
        Request request = requestService.getRequestById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request ID: " + id));

        model.addAttribute("request", request);
        model.addAttribute("user", request.getUser());
        return "request/details"; // fichier details.html dans /templates/requests
    }



    @GetMapping("/delete/{id}")
    public String deleteRequest(@PathVariable Long id) {
        requestService.deleteRequest(id);
        return "redirect:/requests";
    }
}


