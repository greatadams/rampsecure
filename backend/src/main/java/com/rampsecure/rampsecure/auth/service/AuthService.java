package com.rampsecure.rampsecure.auth.service;

import com.rampsecure.rampsecure.auth.dto.LoginRequest;
import com.rampsecure.rampsecure.auth.dto.LoginResponse;
import com.rampsecure.rampsecure.auth.dto.RegisterRequest;
import com.rampsecure.rampsecure.auth.exception.InvalidCredentialsException;
import com.rampsecure.rampsecure.auth.exception.UserAlreadyExistsException;
import com.rampsecure.rampsecure.security.JwtUtil;
import com.rampsecure.rampsecure.user.model.User;
import com.rampsecure.rampsecure.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    //Register
    public LoginResponse register(RegisterRequest registerRequest) {
        // Check username exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        if (userRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())) {
            throw new UserAlreadyExistsException("Phone number already in use");
        }
        // Check passwords match
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new InvalidCredentialsException("Passwords do not match");
        }

        // Map to User object
            User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setFirstName(registerRequest.getFirstName());
        newUser.setLastName(registerRequest.getLastName());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPhoneNumber(registerRequest.getPhoneNumber());
        newUser.setRole(registerRequest.getRole());
        newUser.setStation(registerRequest.getStation());
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setActive(true);


        // Encode password
        String hashPassword= passwordEncoder.encode(registerRequest.getPassword());
        newUser.setPassword(hashPassword);
        //  Save to DB
        userRepository.save(newUser);
        // Return response
        String token =jwtUtil.generateToken(newUser.getUsername(),newUser.getRole().name(), newUser.getId(),newUser.getStation().name());
        return new LoginResponse(newUser.getId(),token, newUser.getUsername(), newUser.getRole(),newUser.getStation(),LocalDateTime.now().plusHours(8));

    }

    //LOGIN
    public  LoginResponse login(LoginRequest loginRequest) {
        log.info("Login attempt for username: {}", loginRequest.getUsername());
        //check if username is in the DB
        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsername(),loginRequest.getUsername())
                .orElseThrow(() -> {
                    log.warn("User not found: {}", loginRequest.getUsername());
                    return new InvalidCredentialsException("Invalid username or password");
                });
        log.info("User found: {}, isActive: {}", user.getUsername(), user.isActive());



        if (!user.isActive()) {
            log.warn("Account inactive: {}", user.getUsername());
            throw new InvalidCredentialsException("Account has been deactivated contact your administrator.");
        }
        //check the password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("Password mismatch for: {}", user.getUsername());
            throw new InvalidCredentialsException("Invalid username or password");
        }
        String token =jwtUtil.generateToken(user.getUsername(),user.getRole().name(), user.getId(),user.getStation().name());
        return new LoginResponse(user.getId(),token, user.getUsername(), user.getRole(),user.getStation(),LocalDateTime.now().plusHours(8));


    }

}
