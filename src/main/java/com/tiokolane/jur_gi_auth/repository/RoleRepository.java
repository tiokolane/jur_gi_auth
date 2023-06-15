package com.tiokolane.jur_gi_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiokolane.jur_gi_auth.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
