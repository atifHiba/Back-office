package com.example.backoffice.dao;

import com.example.backoffice.entity.DonationCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DonationCenterRepository extends JpaRepository<DonationCenter, Long> {
    Optional<DonationCenter> findByName(String name);
    @Query("SELECT c.name, COUNT(r.id) FROM Request r JOIN r.donationCenter c GROUP BY c.name ORDER BY COUNT(r.id) DESC")
    List<Object[]> findTopDonationCenterByRequests();

}
