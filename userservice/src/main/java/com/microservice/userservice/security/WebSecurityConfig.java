package com.microservice.userservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//http
		/*
		 * http .csrf() .disable() .authorizeHttpRequests(auth -> auth
		 * .requestMatchers("/api/auth/**") .permitAll() .requestMatchers("/admin/**")
		 * .hasRole("ADMIN") .anyRequest() .authenticated() );
		 */
		http
			.csrf( csrf -> csrf.disable() )
			.authorizeHttpRequests(
				request -> request
					.requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
					.anyRequest().authenticated()
				)
			.httpBasic(Customizer.withDefaults());
		return http.build();
	}
	
	//@Bean
	/*
	 * public UserDetailsService userDetailsService() { UserDetails user1 =
	 * User.withUsername("manoj") .password("{noop}manoj") .roles("ADMIN") .build();
	 * 
	 * UserDetails user2 = User.withUsername("omkar") .password("{noop}omkar")
	 * .roles("ADMIN") .build(); return new InMemoryUserDetailsManager(user1,
	 * user2); }
	 */
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder(14);
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(bCryptPasswordEncoder());
		return provider;
	}
}
