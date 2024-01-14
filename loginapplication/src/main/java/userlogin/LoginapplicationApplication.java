package userlogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import userlogin.model.Role;
import userlogin.model.User;
import userlogin.repository.UserRepository;

@SpringBootApplication
public class LoginapplicationApplication  implements CommandLineRunner{

	@Autowired
	private UserRepository userRepository;
	public static void main(String[] args) {
		SpringApplication.run(LoginapplicationApplication.class, args);
	}
	
	public void run(String...args) {
		User adminAccount= userRepository.findByRole(Role.ADMIN);
		if(null== adminAccount) {
			User user= new User();
			user.setEmail("admin@gmail.com");
			user.setFirstname("Admin");
			user.setLastname("Admin");
			user.setRole(Role.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
		    userRepository.save(user);
		}
	}

}
