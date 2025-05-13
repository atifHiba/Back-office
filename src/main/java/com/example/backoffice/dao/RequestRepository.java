package com.example.backoffice.dao;

import com.example.backoffice.entity.DonationCenter;
import com.example.backoffice.entity.Request;
import com.example.backoffice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT COUNT(r) FROM Request r WHERE r.requiredBloodUnits = 0")
    long countSaturatedRequests();
    @Query("SELECT CASE WHEN r.requiredBloodUnits = 0 THEN 'Satisfaite' ELSE 'Non satisfaite' END, COUNT(r) FROM Request r GROUP BY CASE WHEN r.requiredBloodUnits = 0 THEN 'Satisfaite' ELSE 'Non satisfaite' END")
    List<Object[]> countRequestSatisfaction();

}
