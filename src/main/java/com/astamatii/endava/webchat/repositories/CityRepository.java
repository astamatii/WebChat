package com.astamatii.endava.webchat.repositories;

import com.astamatii.endava.webchat.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}
