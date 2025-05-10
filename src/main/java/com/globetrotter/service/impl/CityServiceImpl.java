package com.globetrotter.service.impl;

import com.globetrotter.model.City;
import com.globetrotter.repository.CityRepository;
import com.globetrotter.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final Random random = new Random();
    
    // Some sample clues for cities - in a real application, these would come from a database
    private final String[][] cityClues = {
        {"Known for the Statue of Liberty", "Home to Broadway and Times Square", "The Big Apple"},
        {"City of Lights", "Home to the Eiffel Tower", "Known for its fashion and cuisine"},
        {"Home to Big Ben and Westminster Abbey", "Known for its red double-decker buses", "Capital of England"},
        {"Home to the Tokyo Tower", "Capital of Japan", "Known for its cherry blossoms"},
        {"Home to the Sydney Opera House", "Known for its beautiful harbor", "Australia's largest city"},
        {"Home to the Great Pyramids", "Located on the Nile River", "Egypt's capital"},
        {"Famous for its Carnival", "Home to the Christ the Redeemer statue", "Brazil's second-largest city"},
        {"Home to Bollywood", "India's financial capital", "Known for its colonial architecture"},
        {"Home to the Kremlin", "Russia's capital", "Known for its Red Square"},
        {"Home to the Brandenburg Gate", "Germany's capital", "Known for its history and culture"}
    };

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
    public List<City> getCitiesByCountry(String country) {
        return cityRepository.findByCountry(country);
    }

    @Override
    public City saveCity(City city) {
        return cityRepository.save(city);
    }

    @Override
    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }
    
    @Override
    public City getRandomCity() {
        List<City> cities = cityRepository.findAll();
        if (cities.isEmpty()) {
            throw new IllegalStateException("No cities available in the database");
        }
        return cities.get(random.nextInt(cities.size()));
    }
    
    @Override
    public String getRandomClue(Long cityId) {
        // Find the city
        Optional<City> cityOpt = cityRepository.findById(cityId);
        if (cityOpt.isEmpty()) {
            throw new IllegalArgumentException("City not found with ID: " + cityId);
        }
        
        City city = cityOpt.get();
        
        // Find the index of the city in our clues array
        // In a real application, clues would be stored in the database
        int clueIndex = 0;
        
        if (city.getCity().equals("New York")) {
            clueIndex = 0;
        } else if (city.getCity().equals("Paris")) {
            clueIndex = 1;
        } else if (city.getCity().equals("London")) {
            clueIndex = 2;
        } else if (city.getCity().equals("Tokyo")) {
            clueIndex = 3;
        } else if (city.getCity().equals("Sydney")) {
            clueIndex = 4;
        } else if (city.getCity().equals("Cairo")) {
            clueIndex = 5;
        } else if (city.getCity().equals("Rio de Janeiro")) {
            clueIndex = 6;
        } else if (city.getCity().equals("Mumbai")) {
            clueIndex = 7;
        } else if (city.getCity().equals("Moscow")) {
            clueIndex = 8;
        } else if (city.getCity().equals("Berlin")) {
            clueIndex = 9;
        } else {
            // For cities we don't have clues for, generate a generic clue
            return "A major city in " + city.getCountry();
        }
        
        // Return a random clue for this city
        String[] clues = cityClues[clueIndex];
        return clues[random.nextInt(clues.length)];
    }
    
    @Override
    public List<City> getRandomOptions(Long excludeCityId, int count) {
        List<City> allCities = cityRepository.findAll();
        
        // Filter out the city to exclude
        List<City> filteredCities = allCities.stream()
                .filter(city -> !city.getId().equals(excludeCityId))
                .collect(Collectors.toList());
        
        // If we don't have enough cities, return what we have
        if (filteredCities.size() <= count) {
            return new ArrayList<>(filteredCities);
        }
        
        // Shuffle the list and take the first 'count' cities
        List<City> options = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int randomIndex = random.nextInt(filteredCities.size());
            options.add(filteredCities.get(randomIndex));
            filteredCities.remove(randomIndex);
        }
        
        return options;
    }
} 