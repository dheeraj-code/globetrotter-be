package com.globetrotter.service;

import com.globetrotter.model.City;
import java.util.List;
import java.util.Optional;

public interface CityService {
    
    List<City> getAllCities();
    
    Optional<City> getCityById(Long id);
    
    List<City> getCitiesByCountry(String country);
    
    City saveCity(City city);
    
    void deleteCity(Long id);
    
    City getRandomCity();
    
    String getRandomClue(Long cityId);
    
    List<City> getRandomOptions(Long excludeCityId, int count);
} 