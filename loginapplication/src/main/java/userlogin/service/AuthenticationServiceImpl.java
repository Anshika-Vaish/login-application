package userlogin.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import userlogin.dto.JwtAuthenticationResponse;
import userlogin.dto.SignInRequest;
import userlogin.dto.SignUpRequest;
import userlogin.model.Role;
import userlogin.model.User;
import userlogin.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	
	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public User signup(SignUpRequest signUpRequest){
		User user=new User();
		user.setEmail(signUpRequest.getEmail());
		user.setFirstname(signUpRequest.getFirstName());
		user.setLastname(signUpRequest.getLastName());
		user.setRole(Role.USER);
		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		
		
		return userRepository.save(user);
	}
	
	//verify user email nd password
	public JwtAuthenticationResponse signin(SignInRequest signinRequest) {
	
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPasssword()));
	    var user=userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(()-> new IllegalArgumentException("Invalid email or password"));
	    var jwt= jwtService.generateToken(user);
	    var refreshToken= jwtService.generateRefreshToken(new HashMap<>(),user);
        JwtAuthenticationResponse authenticationResponse= new JwtAuthenticationResponse();
        authenticationResponse.setToken(jwt);
        authenticationResponse.setRefreshToken(refreshToken);
	     return authenticationResponse;
	    
	
	}

}
