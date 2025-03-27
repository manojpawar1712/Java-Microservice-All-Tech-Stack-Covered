package com.microservice.userservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.userservice.modal.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	//Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
}
