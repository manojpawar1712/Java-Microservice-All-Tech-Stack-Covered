package com.microservice.userservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.microservice.userservice.dto.AuthRequest;
import com.microservice.userservice.dto.AuthResponse;
import com.microservice.userservice.dto.RegisterRequest;
import com.microservice.userservice.modal.Role;
import com.microservice.userservice.modal.User;
import com.microservice.userservice.repository.UserRepository;

@Service
public class AuthService {
	
	@Autowired
    private UserRepository userRepository;
    
	//@Autowired
	//private PasswordEncoder passwordEncoder;

	public User register(User user) {
		return userRepository.save(user);
	}
	
	
	public String login(AuthRequest authRequest) {
		Optional<User> user = userRepository.findByEmail(authRequest.getEmail());
		if(user.isEmpty()) {
			return "Failure";
		}
		return "Success";
	}
	/*
	 * public AuthResponse register(RegisterRequest request) { if
	 * (userRepository.findByEmail(request.getEmail()).isPresent()) { throw new
	 * RuntimeException("User already exists"); }
	 * 
	 * User user = new User(); user.setFirstName(request.getFirstName());
	 * user.setLastName(request.getLastName()); user.setEmail(request.getEmail());
	 * user.setPassword(passwordEncoder.encode(request.getPassword()));
	 * user.setRole(Role.CUSTOMER);
	 * 
	 * userRepository.save(user);
	 * 
	 * String token = ""; //jwtUtil.generateToken(user.getId(), user.getEmail());
	 * return new AuthResponse(token); }
	 */

	/*
	 * public AuthResponse login(AuthRequest request) { User user =
	 * userRepository.findByEmail(request.getEmail()) .orElseThrow(() -> new
	 * RuntimeException("User not found"));
	 * 
	 * if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
	 * throw new RuntimeException("Invalid credentials"); }
	 * 
	 * String token = "";// jwtUtil.generateToken(user.getId(), user.getEmail());
	 * return new AuthResponse(token); }
	 */
}

