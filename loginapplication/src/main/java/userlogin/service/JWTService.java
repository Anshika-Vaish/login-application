package userlogin.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

import userlogin.model.User;

public interface JWTService {
	
	String generateToken(UserDetails userDetails);
	String extractUserName(String token);
	boolean isTokenValid(String token, UserDetails userDetails);
	String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);

}
