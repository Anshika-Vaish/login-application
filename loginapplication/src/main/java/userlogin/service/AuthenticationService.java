package userlogin.service;

import userlogin.dto.JwtAuthenticationResponse;
import userlogin.dto.SignInRequest;
import userlogin.dto.SignUpRequest;
import userlogin.model.User;

public interface AuthenticationService {
	User signup(SignUpRequest signUpRequest);
	JwtAuthenticationResponse signin(SignInRequest signinRequest);

}
