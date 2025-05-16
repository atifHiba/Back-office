package com.example.backoffice.dao;

import com.example.backoffice.entity.DonationCenter;
import com.example.backoffice.entity.Request;
import com.example.backoffice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT COUNT(r) FROM Request r WHERE r.requiredBloodUnits = 0")
    long countSaturatedRequests();
    @Query("SELECT CASE WHEN r.requiredBloodUnits = 0 THEN 'Satisfaite' ELSE 'Non satisfaite' END, COUNT(r) FROM Request r GROUP BY CASE WHEN r.requiredBloodUnits = 0 THEN 'Satisfaite' ELSE 'Non satisfaite' END")
    List<Object[]> countRequestSatisfaction();
    @Query("SELECT r FROM Request r WHERE FUNCTION('DATE', r.createdAt) = :date")
    List<Request> findRequestsByDate(@Param("date") LocalDate date);
    long countByDonationCenterAndRequiredBloodUnits(DonationCenter donationCenter, int requiredBloodUnits);
    long countByDonationCenter(DonationCenter donationCenter);
    long countByDonationCenterAndRequiredBloodUnitsGreaterThan(DonationCenter center, int requiredBloodUnits); // demandes non saturées (units > 0)
    long count();
    long countByRequiredBloodUnits(int requiredBloodUnits);
    long countByRequiredBloodUnitsGreaterThan(int requiredBloodUnits);
}
