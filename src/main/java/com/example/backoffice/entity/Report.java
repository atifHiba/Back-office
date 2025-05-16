package com.example.backoffice.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String city;

    private String centerName;

    private int requestCount;

    private int donationCount;

    private int registeredUsersCount;

    private boolean opened = false;

    // Constructors, getters and setters

    public Report() {}

    public Report(LocalDate date, String city, String centerName, int requestCount, int donationCount, int registeredUsersCount) {
        this.date = date;
        this.city = city;
        this.centerName = centerName;
        this.requestCount = requestCount;
        this.donationCount = donationCount;
        this.registeredUsersCount = registeredUsersCount;
        this.opened = false;
    }

    public Long getId() { return id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCenterName() { return centerName; }
    public void setCenterName(String centerName) { this.centerName = centerName; }

    public int getRequestCount() { return requestCount; }
    public void setRequestCount(int requestCount) { this.requestCount = requestCount; }

    public int getDonationCount() { return donationCount; }
    public void setDonationCount(int donationCount) { this.donationCount = donationCount; }

    public int getRegisteredUsersCount() { return registeredUsersCount; }
    public void setRegisteredUsersCount(int registeredUsersCount) { this.registeredUsersCount = registeredUsersCount; }

    public boolean isOpened() { return opened; }
    public void setOpened(boolean opened) { this.opened = opened; }
}

