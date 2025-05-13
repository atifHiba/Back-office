package com.example.backoffice.service;

import com.example.backoffice.entity.City;

import java.util.List;
import java.util.Optional;

public interface CityService {
    List<City> getAllCities();
    Optional<City> getCityById(Long id);
    Optional<City> getCityByName(String name);
    City createCity(City city);
    City updateCity(Long id, City updatedCity);
    void deleteCity(Long id);
}
