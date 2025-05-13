package com.example.backoffice.dao;

import com.example.backoffice.entity.Donation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    @Query("SELECT d.donationCenter.name, COUNT(d) FROM Donation d GROUP BY d.donationCenter.name ORDER BY COUNT(d) DESC")
    List<Object[]> findTopDonationCenters(Pageable pageable);

    @Query("SELECT d.donationCenter.name, COUNT(d) FROM Donation d GROUP BY d.donationCenter.name ORDER BY COUNT(d) DESC")
    List<Object[]> findDonationCenters();

}
