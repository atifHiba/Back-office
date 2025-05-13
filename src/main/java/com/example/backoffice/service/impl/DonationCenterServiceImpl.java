package com.example.backoffice.service.impl;

import com.example.backoffice.entity.DonationCenter;
import com.example.backoffice.dao.DonationCenterRepository;
import com.example.backoffice.service.DonationCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DonationCenterServiceImpl implements DonationCenterService {

    private final DonationCenterRepository donationCenterRepository;

    @Autowired
    public DonationCenterServiceImpl(DonationCenterRepository donationCenterRepository) {
        this.donationCenterRepository = donationCenterRepository;
    }

    @Override
    public List<DonationCenter> getAllDonationCenters() {
        return donationCenterRepository.findAll();
    }

    @Override
    public Optional<DonationCenter> getDonationCenterById(Long id) {
        return donationCenterRepository.findById(id);
    }

    @Override
    public Optional<DonationCenter> getDonationCenterByName(String name) {
        return donationCenterRepository.findByName(name);
    }

    @Override
    public DonationCenter createDonationCenter(DonationCenter donationCenter) {
        return donationCenterRepository.save(donationCenter);
    }

    @Override
    public DonationCenter updateDonationCenter(Long id, DonationCenter updatedDonationCenter) {
        return donationCenterRepository.findById(id).map(center -> {
            center.setName(updatedDonationCenter.getName());
            center.setCity(updatedDonationCenter.getCity());
            center.setAddress(updatedDonationCenter.getAddress());
            center.setContactPhone(updatedDonationCenter.getContactPhone());
            center.setType(updatedDonationCenter.getType());
            return donationCenterRepository.save(center);
        }).orElseThrow(() -> new RuntimeException("Donation center not found"));
    }

    @Override
    public void deleteDonationCenter(Long id) {
        donationCenterRepository.deleteById(id);
    }
    public Object[] getTopDonationCenterByRequests() {
        List<Object[]> results = donationCenterRepository.findTopDonationCenterByRequests();
        return results.isEmpty() ? null : results.get(0); // On récupère juste le premier (le meilleur)
    }





}

