package com.example.backoffice.service.impl;

import com.example.backoffice.dao.RequestRepository;
import com.example.backoffice.entity.DonationCenter;
import com.example.backoffice.entity.Request;
import com.example.backoffice.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    @Override
    public Optional<Request> getRequestById(Long id) {
        return requestRepository.findById(id);
    }

    @Override
    public Request createRequest(Request request) {
        return requestRepository.save(request);
    }

    @Override
    public void deleteRequest(Long id) {
        requestRepository.deleteById(id);
    }

    @Override
    public List<Request> getRequestsByCenterId(Long centerId) {
        return requestRepository.findByDonationCenterId(centerId);
    }


    @Override
    public List<Request> getRequestsByRequiredBloodUnits(int units) {
        return requestRepository.findByRequiredBloodUnits(units);
    }

    @Override
    public List<Request> getRequestsByRequiredBloodUnitsGreaterThan(int units) {
        return requestRepository.findByRequiredBloodUnitsGreaterThan(units);
    }

    @Override
    public List<Request> getRequestsByCenterIdAndRequiredBloodUnits(Long centerId, int units) {
        return requestRepository.findByDonationCenterIdAndRequiredBloodUnits(centerId, units);
    }

    @Override
    public List<Request> getRequestsByCenterIdAndRequiredBloodUnitsGreaterThan(Long centerId, int units) {
        return requestRepository.findByDonationCenterIdAndRequiredBloodUnitsGreaterThan(centerId, units);
    }
}


