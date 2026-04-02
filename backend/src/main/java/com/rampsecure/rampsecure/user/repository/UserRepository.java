package com.rampsecure.rampsecure.user.repository;

import com.rampsecure.rampsecure.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String Username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    List<User> findAllByUsername(String username);
    List<User> findAllByEmail(String email);
    List<User> findAllByFirstNameAndLastName(String firstName, String lastName);
}
