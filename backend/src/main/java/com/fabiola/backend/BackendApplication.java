package com.fabiola.backend;

import java.io.File;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fabiola.backend.entities.User;
import com.fabiola.backend.entities.enums.Role;
import com.fabiola.backend.repository.UserRepository;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	public void run(String... args) {
		List<User> adminAccount = userRepository.findByRole(Role.ADMIN);
		if (adminAccount.isEmpty()) {
			User user = new User();
			user.setRole(Role.ADMIN);
			user.setEmail("admin@gmail.com");
			user.setFirstname("Admin");
			user.setLastname("Manager");
			user.setPhoneNumber("0789479289");
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			userRepository.save(user);
		}
	}
}