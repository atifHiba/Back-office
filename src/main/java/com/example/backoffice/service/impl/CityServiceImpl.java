package com.example.backoffice.service.impl;

import com.example.backoffice.dao.CityRepository;
import com.example.backoffice.entity.City;
import com.example.backoffice.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @Override
    public Optional<City> getCityById(Long id) {
        return cityRepository.findById(id);
    }

    @Override
    public Optional<City> getCityByName(String name) {
        return cityRepository.findByName(name);
    }

    @Override
    public City createCity(City city) {
        return cityRepository.save(city);
    }

    @Override
    public City updateCity(Long id, City updatedCity) {
        return cityRepository.findById(id)
                .map(city -> {
                    city.setName(updatedCity.getName());
                    return cityRepository.save(city);
                })
                .orElseThrow(() -> new RuntimeException("City not found"));
    }

    @Override
    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }
}
