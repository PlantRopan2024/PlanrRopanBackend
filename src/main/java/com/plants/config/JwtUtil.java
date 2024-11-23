package com.plants.config;

import java.util.Date;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import java.util.function.Function;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	// private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Replace with a secure key
	 private final String SECRET_KEY = "abcgAbcgaassssssaaaaaaaaaaaaaaaaaaaaaaaaasssssssssssssssssssssssssssssssssssssssssssssssssaaawwwwqqqq";

	    public String generateToken(String mobileNumber) {
	        return Jwts.builder()
	                .setSubject(mobileNumber)
	                .setIssuedAt(new Date())
	              //  .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiry
	                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
	                .compact();
	    }

	    public Boolean validateToken(String token, String mobileNumber) {
	        final String username = extractUsername(token);
	        return (username.equals(mobileNumber) && !isTokenExpired(token));
	    }

	  //  public String extractUsername(String token) {
	//        return extractClaim(token, Claims::getSubject);
	//    }
	    
	    public String extractUsername(String token) {
	        return Jwts.parserBuilder()
	                   .setSigningKey(SECRET_KEY)
	                   .build()
	                   .parseClaimsJws(token)
	                   .getBody()
	                   .getSubject();
	    }
	    public Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	    }

	    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	        return claimsResolver.apply(claims);
	    }

	    private Boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }
	
}
