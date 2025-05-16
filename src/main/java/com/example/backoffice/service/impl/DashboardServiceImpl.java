package com.example.backoffice.service.impl;


import com.example.backoffice.dao.*;
import com.example.backoffice.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DonationCenterRepository donationCenterRepository;

    @Override
    public long getTotalRequests() {
        return requestRepository.count();
    }

    @Override
    public long getTotalDonations() {
        return donationRepository.count();
    }

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public long getTotalDonationCenters() {
        return donationCenterRepository.count();
    }
}
