package com.example.backoffice.service.impl;

import com.example.backoffice.dao.RequestRepository;
import com.example.backoffice.entity.Donation;
import com.example.backoffice.dao.DonationRepository;
import com.example.backoffice.entity.Request;
import com.example.backoffice.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonationServiceImpl implements DonationService {
    private final RequestRepository requestRepository;
    private final DonationRepository donationRepository;

    @Autowired
    public DonationServiceImpl(DonationRepository donationRepository, RequestRepository requestRepository) {
        this.donationRepository = donationRepository;
        this.requestRepository = requestRepository;
    }

    public void saveDonation(Donation donation) {
        donationRepository.save(donation);

        Request request = donation.getRequest();
        if (request != null) {
            int volumeToSubtract = donation.getVolumeMl(); // ex: 450 ml
            int newVolume = request.getRequiredBloodUnits() - volumeToSubtract;

            request.setRequiredBloodUnits(Math.max(0, newVolume)); // éviter négatif
            requestRepository.save(request);
        }
    }


    @Override
    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }

    @Override
    public Donation getDonationById(Long id) {
        return donationRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteDonation(Long id) {
        donationRepository.deleteById(id);
    }

    public List<Donation> findAllDonations() {
        return donationRepository.findAll();
    }

    // Récupérer un don par son ID
    public Donation findDonationById(Long donationId) {
        return donationRepository.findById(donationId).orElse(null);
    }

    public long getTotalDonors() {
        return donationRepository.count();
    }

    public long getTotalDonations() {
        List<Donation> donations = donationRepository.findAll(); // Récupère toutes les donations
        return donations.size(); // Retourne le nombre de dons
    }

}
