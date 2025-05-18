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
		try {
			// Create upload directory if it doesn't exist
			new File("/tmp/uploads").mkdirs();

			// Enhanced startup logging
			System.out.println("Starting application with PID: " + ProcessHandle.current().pid());
			System.out.println("Java version: " + System.getProperty("java.version"));
			System.out.println("Memory: " + Runtime.getRuntime().maxMemory() / (1024 * 1024) + "MB max");

			SpringApplication.run(BackendApplication.class, args);
			System.out.println("Application started successfully");
		} catch (Exception e) {
			System.err.println("Application failed to start");
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void run(String... args) {
		System.out.println("=== Initializing Admin User ===");
		try {
			// Using findByRole instead of countByRole
			List<User> adminUsers = userRepository.findByRole(Role.ADMIN);
			if (adminUsers.isEmpty()) {
				User admin = new User();
				admin.setEmail("admin@gmail.com");
				admin.setFirstname("Admin");
				admin.setLastname("Manager");
				admin.setPhoneNumber("0789479289");
				admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
				admin.setRole(Role.ADMIN);

				userRepository.save(admin);
				System.out.println("Admin user created successfully");
			} else {
				System.out.println("Admin user already exists");
			}
		} catch (Exception e) {
			System.err.println("ERROR during admin initialization:");
			e.printStackTrace();
			// Continue startup even if admin creation fails
		}
	}

	// Add this if you're still getting PostConstruct error
	// @javax.annotation.PostConstruct
	public void init() {
		// Additional initialization if needed
		System.out.println("Application initialization complete");
	}
}