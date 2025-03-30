package com.microservice.userservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.microservice.userservice.modal.User;
import com.microservice.userservice.repository.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userrepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userrepository.findByEmail(username);
		if(user.isEmpty()) {
			throw new UsernameNotFoundException("User not found");
		}
		User u = user.get();
		return 
				new org.springframework.security.core.userdetails.User(
						u.getEmail(),
						u.getPassword(),
						List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name())));
	}

}
