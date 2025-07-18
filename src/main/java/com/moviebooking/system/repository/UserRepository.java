package com.moviebooking.system.repository;

import com.moviebooking.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Basic Queries
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Role Queries
    List<User> findByRole(User.Role role);
    List<User> findByRoleOrderByUsername(User.Role role);
    long countByRole(User.Role role);

    // Status Queries
    List<User> findByIsEnabledTrue();
    List<User> findByIsEnabledFalse();
    List<User> findByIsEnabledTrueOrderByUsername();
    List<User> findByIsEnabledFalseOrderByUsername();
    long countByIsEnabledTrue();
    long countByIsEnabledFalse();

    // Search Queries
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchUsers(@Param("keyword") String keyword);

    List<User> findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String username, String fullName, String email);

    List<User> findByFullNameContainingIgnoreCase(String fullName);
    List<User> findByUsernameContainingIgnoreCase(String username);

    // Phone Number Queries
    List<User> findByPhoneNumberIsNotNull();
    List<User> findByPhoneNumberIsNull();
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
}
