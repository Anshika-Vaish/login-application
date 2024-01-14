package userlogin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import userlogin.model.Role;
import userlogin.service.UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor 
public class SecurityConfiguration {
	
	@Autowired
	private  JWTAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
	private  UserService userService;
	
	
	//if role admin then allow
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
		.authorizeHttpRequests(request -> request.requestMatchers("api/v1/auth/**").permitAll()
				.requestMatchers("api/v1/admin")
				.hasAnyAuthority(Role.ADMIN.name())
				.requestMatchers("api/v1/user")
				.hasAnyAuthority(Role.USER.name())
				.anyRequest().authenticated())
		         .sessionManagement(manager-> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		         .authenticationProvider(authenticationProvider()).addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
		        //gave authentication provider to spring security
		         return http.build();
		
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userService.userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
		
	}
	
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
    	return config.getAuthenticationManager();
    }
}
