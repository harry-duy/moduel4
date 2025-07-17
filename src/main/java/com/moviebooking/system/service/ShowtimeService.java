package com.moviebooking.system.service;

import com.moviebooking.system.entity.Showtime;
import com.moviebooking.system.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public Optional<Showtime> getShowtimeById(Long id) {
        return showtimeRepository.findById(id);
    }

    public Showtime saveShowtime(Showtime showtime) {
        return showtimeRepository.save(showtime);
    }

    public void deleteShowtime(Long id) {
        showtimeRepository.deleteById(id);
    }

    public List<Showtime> getShowtimesByMovie(Long movieId) {
        return showtimeRepository.findUpcomingShowtimesByMovieId(movieId, LocalDateTime.now());
    }

    public List<Showtime> getUpcomingShowtimes() {
        return showtimeRepository.findAllUpcomingShowtimes(LocalDateTime.now());
    }

    public boolean updateAvailableSeats(Long showtimeId, Integer bookedSeats) {
        Optional<Showtime> showtimeOpt = showtimeRepository.findById(showtimeId);
        if (showtimeOpt.isPresent()) {
            Showtime showtime = showtimeOpt.get();
            if (showtime.getAvailableSeats() >= bookedSeats) {
                showtime.setAvailableSeats(showtime.getAvailableSeats() - bookedSeats);
                showtimeRepository.save(showtime);
                return true;
            }
        }
        return false;
    }
}