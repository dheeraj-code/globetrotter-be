package com.globetrotter.repository;

import com.globetrotter.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    
    List<City> findByCity(String city);
    
    List<City> findByCountry(String country);
} 