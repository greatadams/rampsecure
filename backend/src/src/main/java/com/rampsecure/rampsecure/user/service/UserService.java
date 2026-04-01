package com.rampsecure.rampsecure.user.service;

import com.rampsecure.rampsecure.user.dto.UserResponse;
import com.rampsecure.rampsecure.user.model.Role;
import com.rampsecure.rampsecure.user.model.Station;
import com.rampsecure.rampsecure.user.model.User;
import com.rampsecure.rampsecure.user.repository.UserRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private User user;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //get all users
    //Gets all users from DB
   // Streams through each one
   // Maps each User entity to a UserResponse DTO
   // Collects back to a list
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .filter(User::isActive)
                .map(user -> new UserResponse(
                  user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getStation(),
                        user.getRole()
                ))
                .toList();
    }

// update role/station
    public UserResponse updateUser(UUID id, Role role, Station station) {
        //check user exists
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setRole(role);
        existingUser.setStation(station);

        User saved= userRepository.save(existingUser);
        return new UserResponse(
                saved.getId(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getStation(),
                saved.getRole()
        );

    }

    //delete users
    public void deleteUser(UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setActive(false);
        userRepository.save(existingUser);
    }

}
