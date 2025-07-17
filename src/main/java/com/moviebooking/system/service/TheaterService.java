package com.moviebooking.system.service;

import com.moviebooking.system.entity.Theater;
import com.moviebooking.system.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;

    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    public Optional<Theater> getTheaterById(Long id) {
        return theaterRepository.findById(id);
    }

    public Theater saveTheater(Theater theater) {
        return theaterRepository.save(theater);
    }

    public void deleteTheater(Long id) {
        theaterRepository.deleteById(id);
    }

    public List<Theater> getTheatersByCity(String city) {
        return theaterRepository.findByCity(city);
    }

    public List<Theater> searchTheaters(String name) {
        return theaterRepository.findByNameContainingIgnoreCase(name);
    }
}