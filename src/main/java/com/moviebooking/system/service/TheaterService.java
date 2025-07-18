package com.moviebooking.system.service;

import com.moviebooking.system.entity.Theater;
import com.moviebooking.system.exception.ResourceNotFoundException;
import com.moviebooking.system.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;

    // Basic CRUD Operations
    @Cacheable("theaters")
    public List<Theater> getAllTheaters() {
        return theaterRepository.findByOrderByName();
    }

    public Page<Theater> getTheatersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        return theaterRepository.findAll(pageable);
    }

    public Optional<Theater> getTheaterById(Long id) {
        return theaterRepository.findById(id);
    }

    public Theater getTheaterByIdOrThrow(Long id) {
        return theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + id));
    }

    @Transactional
    public Theater saveTheater(Theater theater) {
        validateTheater(theater);
        return theaterRepository.save(theater);
    }

    @Transactional
    public Theater updateTheater(Long id, Theater theaterDetails) {
        Theater theater = getTheaterByIdOrThrow(id);

        theater.setName(theaterDetails.getName());
        theater.setAddress(theaterDetails.getAddress());
        theater.setCity(theaterDetails.getCity());
        theater.setPhoneNumber(theaterDetails.getPhoneNumber());
        theater.setTotalSeats(theaterDetails.getTotalSeats());

        return theaterRepository.save(theater);
    }

    @Transactional
    public void deleteTheater(Long id) {
        if (!theaterRepository.existsById(id)) {
            throw new ResourceNotFoundException("Theater not found with id: " + id);
        }
        theaterRepository.deleteById(id);
    }

    // Query Methods
    public List<Theater> getTheatersByCity(String city) {
        return theaterRepository.findByCityOrderByName(city);
    }

    public List<Theater> searchTheaters(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllTheaters();
        }
        return theaterRepository.searchTheaters(keyword.trim());
    }

    public List<Theater> getTheatersByCapacityRange(Integer minSeats, Integer maxSeats) {
        return theaterRepository.findByTotalSeatsBetween(minSeats, maxSeats);
    }

    // Statistics
    public long getTotalTheatersCount() {
        return theaterRepository.count();
    }

    public long getTheatersCountByCity(String city) {
        return theaterRepository.countByCity(city);
    }

    public List<String> getAllCities() {
        return theaterRepository.findDistinctCities();
    }

    public Double getAverageCapacity() {
        return theaterRepository.findAverageCapacity();
    }

    public Long getTotalCapacity() {
        return theaterRepository.findTotalCapacity();
    }

    // Validation
    private void validateTheater(Theater theater) {
        if (theater.getName() == null || theater.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Theater name cannot be empty");
        }

        if (theater.getAddress() == null || theater.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Theater address cannot be empty");
        }

        if (theater.getCity() == null || theater.getCity().trim().isEmpty()) {
            throw new IllegalArgumentException("Theater city cannot be empty");
        }

        if (theater.getTotalSeats() == null || theater.getTotalSeats() <= 0) {
            throw new IllegalArgumentException("Theater must have positive number of seats");
        }

        if (theater.getTotalSeats() > 1000) {
            throw new IllegalArgumentException("Theater capacity cannot exceed 1000 seats");
        }
    }

    // Business Logic Methods
    public boolean hasAvailableCapacity(Long theaterId, Integer requiredSeats) {
        Theater theater = getTheaterByIdOrThrow(theaterId);
        return theater.getTotalSeats() >= requiredSeats;
    }

    public List<Theater> getTheatersWithMinimumCapacity(Integer minCapacity) {
        return theaterRepository.findByTotalSeatsGreaterThanEqual(minCapacity);
    }
}