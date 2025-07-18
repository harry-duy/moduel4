package com.moviebooking.system.service;

import com.moviebooking.system.entity.Showtime;
import com.moviebooking.system.entity.Movie;
import com.moviebooking.system.entity.Theater;
import com.moviebooking.system.exception.ResourceNotFoundException;
import com.moviebooking.system.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private TheaterService theaterService;

    // Basic CRUD Operations
    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll(Sort.by(Sort.Direction.ASC, "showTime"));
    }

    public Page<Showtime> getShowtimesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "showTime"));
        return showtimeRepository.findAll(pageable);
    }

    public Optional<Showtime> getShowtimeById(Long id) {
        return showtimeRepository.findById(id);
    }

    public Showtime getShowtimeByIdOrThrow(Long id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + id));
    }

    @Transactional
    public Showtime saveShowtime(Showtime showtime) {
        validateShowtime(showtime);

        // Set available seats to theater capacity if not set
        if (showtime.getAvailableSeats() == null) {
            showtime.setAvailableSeats(showtime.getTheater().getTotalSeats());
        }

        return showtimeRepository.save(showtime);
    }

    @Transactional
    public Showtime createShowtime(Long movieId, Long theaterId, LocalDateTime showTime, Double ticketPrice) {
        Movie movie = movieService.getMovieByIdOrThrow(movieId);
        Theater theater = theaterService.getTheaterByIdOrThrow(theaterId);

        Showtime showtime = new Showtime(movie, theater, showTime, ticketPrice);
        return saveShowtime(showtime);
    }

    @Transactional
    public Showtime updateShowtime(Long id, Showtime showtimeDetails) {
        Showtime showtime = getShowtimeByIdOrThrow(id);

        showtime.setShowTime(showtimeDetails.getShowTime());
        showtime.setTicketPrice(showtimeDetails.getTicketPrice());

        if (showtimeDetails.getMovie() != null) {
            showtime.setMovie(showtimeDetails.getMovie());
        }

        if (showtimeDetails.getTheater() != null) {
            showtime.setTheater(showtimeDetails.getTheater());
            // Reset available seats if theater changed
            showtime.setAvailableSeats(showtimeDetails.getTheater().getTotalSeats());
        }

        return showtimeRepository.save(showtime);
    }

    @Transactional
    public void deleteShowtime(Long id) {
        if (!showtimeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Showtime not found with id: " + id);
        }
        showtimeRepository.deleteById(id);
    }

    // Query Methods
    public List<Showtime> getShowtimesByMovie(Long movieId) {
        return showtimeRepository.findUpcomingShowtimesByMovieIdOrderByShowTime(movieId, LocalDateTime.now());
    }

    public List<Showtime> getShowtimesByTheater(Long theaterId) {
        return showtimeRepository.findUpcomingShowtimesByTheaterIdOrderByShowTime(theaterId, LocalDateTime.now());
    }

    public List<Showtime> getShowtimesByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return showtimeRepository.findByShowTimeBetweenOrderByShowTime(startOfDay, endOfDay);
    }

    public List<Showtime> getUpcomingShowtimes() {
        return showtimeRepository.findAllUpcomingShowtimesOrderByShowTime(LocalDateTime.now());
    }

    public List<Showtime> getUpcomingShowtimes(int days) {
        LocalDateTime end = LocalDateTime.now().plusDays(days);
        return showtimeRepository.findByShowTimeBetweenOrderByShowTime(LocalDateTime.now(), end);
    }

    public Map<Theater, List<Showtime>> getShowtimesByMovieGroupedByTheater(Long movieId) {
        List<Showtime> showtimes = getShowtimesByMovie(movieId);
        return showtimes.stream()
                .collect(Collectors.groupingBy(Showtime::getTheater));
    }

    public Map<LocalDate, List<Showtime>> getShowtimesByMovieGroupedByDate(Long movieId) {
        List<Showtime> showtimes = getShowtimesByMovie(movieId);
        return showtimes.stream()
                .collect(Collectors.groupingBy(showtime -> showtime.getShowTime().toLocalDate()));
    }

    // Seat Management
    @Transactional
    public boolean hasSufficientSeats(Long showtimeId, Integer requiredSeats) {
        Showtime showtime = getShowtimeByIdOrThrow(showtimeId);
        return showtime.getAvailableSeats() >= requiredSeats;
    }

    @Transactional
    public boolean reserveSeats(Long showtimeId, Integer numberOfSeats) {
        Showtime showtime = getShowtimeByIdOrThrow(showtimeId);

        if (showtime.getAvailableSeats() >= numberOfSeats) {
            showtime.setAvailableSeats(showtime.getAvailableSeats() - numberOfSeats);
            showtimeRepository.save(showtime);
            return true;
        }
        return false;
    }

    @Transactional
    public void releaseSeats(Long showtimeId, Integer numberOfSeats) {
        Showtime showtime = getShowtimeByIdOrThrow(showtimeId);
        int newAvailableSeats = showtime.getAvailableSeats() + numberOfSeats;

        // Don't exceed theater capacity
        int maxSeats = showtime.getTheater().getTotalSeats();
        showtime.setAvailableSeats(Math.min(newAvailableSeats, maxSeats));
        showtimeRepository.save(showtime);
    }

    // Statistics
    public long getTotalShowtimesCount() {
        return showtimeRepository.count();
    }

    public long getUpcomingShowtimesCount() {
        return showtimeRepository.countByShowTimeAfter(LocalDateTime.now());
    }

    public Double getAverageTicketPrice() {
        return showtimeRepository.findAverageTicketPrice();
    }

    public List<Showtime> getMostPopularShowtimes(int limit) {
        // Showtimes with least available seats (most booked)
        return showtimeRepository.findMostPopularShowtimes(PageRequest.of(0, limit));
    }

    // Validation
    private void validateShowtime(Showtime showtime) {
        if (showtime.getMovie() == null) {
            throw new IllegalArgumentException("Movie cannot be null");
        }

        if (showtime.getTheater() == null) {
            throw new IllegalArgumentException("Theater cannot be null");
        }

        if (showtime.getShowTime() == null) {
            throw new IllegalArgumentException("Show time cannot be null");
        }

        if (showtime.getShowTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Show time cannot be in the past");
        }

        if (showtime.getTicketPrice() == null || showtime.getTicketPrice() <= 0) {
            throw new IllegalArgumentException("Ticket price must be positive");
        }

        // Check for conflicts (same theater at the same time)
        List<Showtime> conflictingShowtimes = showtimeRepository.findConflictingShowtimes(
                showtime.getTheater().getId(),
                showtime.getShowTime().minusHours(1),
                showtime.getShowTime().plusHours(3), // Assuming 3-hour buffer
                showtime.getId() != null ? showtime.getId() : -1L
        );

        if (!conflictingShowtimes.isEmpty()) {
            throw new IllegalArgumentException("Theater is already booked for this time slot");
        }
    }

    // Bulk Operations
    @Transactional
    public List<Showtime> createMultipleShowtimes(Long movieId, Long theaterId,
                                                  List<LocalDateTime> showTimes, Double ticketPrice) {
        Movie movie = movieService.getMovieByIdOrThrow(movieId);
        Theater theater = theaterService.getTheaterByIdOrThrow(theaterId);

        return showTimes.stream()
                .map(showTime -> {
                    Showtime showtime = new Showtime(movie, theater, showTime, ticketPrice);
                    return saveShowtime(showtime);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateTicketPricesForMovie(Long movieId, Double newPrice) {
        List<Showtime> showtimes = getShowtimesByMovie(movieId);
        showtimes.forEach(showtime -> {
            showtime.setTicketPrice(newPrice);
            showtimeRepository.save(showtime);
        });
    }
}
