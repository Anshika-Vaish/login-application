package userlogin.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import userlogin.model.User;

@Service
public class JWTServiceimpl implements JWTService{
	
	public String generateToken(UserDetails userDetails) {
		return Jwts.builder().setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()* 1000 *60*24))
				.signWith(getSiginKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	private Key getSiginKey() {
		byte[] key= Decoders.BASE64.decode("ca9534be-03d0-4ef3-82a1-213d37e32c31");
		return Keys.hmacShaKeyFor(key);
		
	}
	
	//to extract username from token
	
	private <T> T extractClaim(String token, Function<Claims,T> claimsResolvers) {
		final Claims claims=extractAllClaims(token);
		return claimsResolvers.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSiginKey()).build().parseClaimsJws(token).getBody();
	}
	
	public String extractUserName(String token) {
		return extractClaim(token,Claims::getSubject);//to get email
		
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username=extractUserName(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	private boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
		
	}
	@Override
	public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()* 604800000))
				.signWith(getSiginKey(), SignatureAlgorithm.HS256)
				.compact();

	}
}
