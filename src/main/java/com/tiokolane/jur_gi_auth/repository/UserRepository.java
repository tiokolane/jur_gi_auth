package com.tiokolane.jur_gi_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiokolane.jur_gi_auth.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    List<User> findAll();
    
}