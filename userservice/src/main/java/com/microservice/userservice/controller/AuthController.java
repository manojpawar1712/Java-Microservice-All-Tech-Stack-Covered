package com.microservice.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.userservice.dto.AuthRequest;
import com.microservice.userservice.dto.AuthResponse;
import com.microservice.userservice.dto.RegisterRequest;
import com.microservice.userservice.modal.User;
import com.microservice.userservice.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;

	private final BCryptPasswordEncoder encoder;

	@Autowired
	AuthenticationProvider authenticationProvider;

	public AuthController(AuthService authService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.authService = authService;
		this.encoder = bCryptPasswordEncoder;
	}

	@PostMapping("/register")
	public RegisterRequest register(@RequestBody RegisterRequest user) {
		User usr = new User();
		usr.setFirstName(user.getFirstName());
		usr.setLastName(user.getLastName());
		usr.setEmail(user.getEmail());
		usr.setPassword(encoder.encode(user.getPassword()));
		usr.setRole(user.getRole());
		authService.register(usr);
		return user;
	}

	@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest ) throws Exception {
		try {
			authenticationProvider
			.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));			
			return ResponseEntity.ok("Authentication Success");
		} catch(AuthenticationException  e) {
			System.out.println("User name Pass Missmatch");
			return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\": \"Unauthorized\", \"message\": \"Invalid username or password\"}");
		}
		//Login success then proceed with token generation
    }

	@GetMapping("/test")
	public String Hello() {
		return "Test Succes";
	}
}
