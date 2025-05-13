package com.example.backoffice.service;

import com.example.backoffice.entity.DonationCenter;
import com.example.backoffice.entity.Request;

import java.util.List;
import java.util.Optional;

public interface RequestService {
    List<Request> getAllRequests();
    Optional<Request> getRequestById(Long id);
    Request createRequest(Request request);
    void deleteRequest(Long id);

}
