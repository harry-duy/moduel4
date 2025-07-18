package com.moviebooking.system.service;

import com.moviebooking.system.entity.User;
import com.moviebooking.system.exception.UserAlreadyExistsException;
import com.moviebooking.system.exception.ResourceNotFoundException;
import com.moviebooking.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.security.password.min-length:6}")
    private int minPasswordLength;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[+]?[0-9]{10,15}$"
    );

    // Basic CRUD Operations
    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Page<User> getUsersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return userRepository.findAll(pageable);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User getUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserByUsernameOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // User Registration and Management
    @Transactional
    public User saveUser(User user) {
        validateUser(user);

        // Check for existing username
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + user.getUsername());
        }

        // Check for existing email
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + user.getEmail());
        }

        // Hash password if it's not already hashed
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // Set default values
        if (user.getIsEnabled() == null) {
            user.setIsEnabled(true);
        }

        if (user.getRole() == null) {
            user.setRole(User.Role.USER);
        }

        return userRepository.save(user);
    }

    @Transactional
    public User registerUser(String username, String password, String email, String fullName, String phoneNumber) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setRole(User.Role.USER);
        user.setIsEnabled(true);

        return saveUser(user);
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = getUserByIdOrThrow(id);

        // Update basic fields
        if (userDetails.getUsername() != null && !userDetails.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(userDetails.getUsername())) {
                throw new UserAlreadyExistsException("Username already exists: " + userDetails.getUsername());
            }
            user.setUsername(userDetails.getUsername());
        }

        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new UserAlreadyExistsException("Email already exists: " + userDetails.getEmail());
            }
            user.setEmail(userDetails.getEmail());
        }

        if (userDetails.getFullName() != null) {
            user.setFullName(userDetails.getFullName());
        }

        if (userDetails.getPhoneNumber() != null) {
            user.setPhoneNumber(userDetails.getPhoneNumber());
        }

        if (userDetails.getRole() != null) {
            user.setRole(userDetails.getRole());
        }

        if (userDetails.getIsEnabled() != null) {
            user.setIsEnabled(userDetails.getIsEnabled());
        }

        return userRepository.save(user);
    }

    @Transactional
    public User updateProfile(Long id, String fullName, String phoneNumber) {
        User user = getUserByIdOrThrow(id);
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long id, String currentPassword, String newPassword) {
        User user = getUserByIdOrThrow(id);

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Validate new password
        validatePassword(newPassword);

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void resetPassword(String email, String newPassword) {
        User user = getUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        validatePassword(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void disableUser(Long id) {
        User user = getUserByIdOrThrow(id);
        user.setIsEnabled(false);
        userRepository.save(user);
    }

    @Transactional
    public void enableUser(Long id) {
        User user = getUserByIdOrThrow(id);
        user.setIsEnabled(true);
        userRepository.save(user);
    }

    // Query Methods
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role);
    }

    public List<User> getEnabledUsers() {
        return userRepository.findByIsEnabledTrue();
    }

    public List<User> getDisabledUsers() {
        return userRepository.findByIsEnabledFalse();
    }

    public List<User> searchUsers(String keyword) {
        return userRepository.findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                keyword, keyword, keyword);
    }

    // Statistics
    public long getTotalUsersCount() {
        return userRepository.count();
    }

    public long getActiveUsersCount() {
        return userRepository.countByIsEnabledTrue();
    }

    public long getAdminUsersCount() {
        return userRepository.countByRole(User.Role.ADMIN);
    }

    public long getRegularUsersCount() {
        return userRepository.countByRole(User.Role.USER);
    }

    // Validation Methods
    private void validateUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (user.getUsername().length() < 3 || user.getUsername().length() > 50) {
            throw new IllegalArgumentException("Username must be between 3 and 50 characters");
        }

        if (user.getPassword() != null) {
            validatePassword(user.getPassword());
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }

        if (user.getPhoneNumber() != null && !user.getPhoneNumber().trim().isEmpty()) {
            if (!PHONE_PATTERN.matcher(user.getPhoneNumber().replaceAll("\\s+", "")).matches()) {
                throw new IllegalArgumentException("Invalid phone number format");
            }
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < minPasswordLength) {
            throw new IllegalArgumentException("Password must be at least " + minPasswordLength + " characters long");
        }

        if (password.length() > 100) {
            throw new IllegalArgumentException("Password cannot be longer than 100 characters");
        }

        // Additional password strength validation can be added here
    }

    // Security and Authentication
    public boolean verifyPassword(String username, String password) {
        Optional<User> userOpt = getUserByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getIsEnabled()) {
            return passwordEncoder.matches(password, userOpt.get().getPassword());
        }
        return false;
    }

    public boolean isUserEnabled(String username) {
        return getUserByUsername(username)
                .map(User::getIsEnabled)
                .orElse(false);
    }

    public boolean isUserAdmin(String username) {
        return getUserByUsername(username)
                .map(user -> user.getRole() == User.Role.ADMIN)
                .orElse(false);
    }
}
