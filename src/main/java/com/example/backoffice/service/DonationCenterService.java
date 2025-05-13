package com.example.backoffice.service;

import com.example.backoffice.entity.DonationCenter;

import java.util.List;
import java.util.Optional;

public interface DonationCenterService {
    List<DonationCenter> getAllDonationCenters();
    Optional<DonationCenter> getDonationCenterById(Long id);
    Optional<DonationCenter> getDonationCenterByName(String name);
    DonationCenter createDonationCenter(DonationCenter donationCenter);
    DonationCenter updateDonationCenter(Long id, DonationCenter updatedDonationCenter);
    void deleteDonationCenter(Long id);
    Object[] getTopDonationCenterByRequests();
}
