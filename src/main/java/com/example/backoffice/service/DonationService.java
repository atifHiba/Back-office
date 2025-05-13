package com.example.backoffice.service;

import com.example.backoffice.entity.Donation;

import java.util.List;

public interface DonationService {
    void saveDonation(Donation donation);
    List<Donation> getAllDonations();
    Donation getDonationById(Long id);
    void deleteDonation(Long id);
     List<Donation> findAllDonations();
    Donation findDonationById(Long donationId);
    long getTotalDonors();
    long getTotalDonations();
}
