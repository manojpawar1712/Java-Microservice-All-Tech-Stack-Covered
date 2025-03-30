package com.microservice.userservice.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.microservice.userservice.dto.AuthRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secretKey;
	
	public String generateToken(AuthRequest authRequest, List<String> roles) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", roles);
		return Jwts
				.builder()
				.claims()
				.add(claims)
				.subject(authRequest.getEmail())
				.issuer("MANOJ")
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 60*10*1000))
				.and()
				.signWith(generateKey())
				.compact();
		
	}
	
	private SecretKey generateKey() {
		byte[] decode = Decoders.BASE64.decode((getSecretKey()));
		return Keys.hmacShaKeyFor(decode);
	}

	public String getSecretKey() {
		return secretKey;
	}

	public String extractUserName(String token) {
		return extractClaims(token, Claims:: getSubject);
	}

	private <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
		Claims claims = extratClaims(token);
		return claimResolver.apply(claims);
	}

	private Claims extratClaims(String token) {
		return Jwts.parser()
				.verifyWith(generateKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public boolean isTokenVald(String jwtToken, UserDetails userDetails) {
		final String username = extractUserName(jwtToken);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
	}

	private boolean isTokenExpired(String jwtToken) {
		return extractExpiration(jwtToken).before(new Date());
	}

	private Date extractExpiration(String jwtToken) {
		return extractClaims(jwtToken, Claims::getExpiration);
	}

}
