package com.example.pweb_backend;

import com.example.pweb_backend.model.User;
import com.example.pweb_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PwebBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PwebBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner initSuperAdmin(UserRepository userRepository) {
		return args -> {
			String superAdminEmail = "superadmin@tienda.com";

			if (userRepository.existsByEmail(superAdminEmail)) {
				return; // ya existe, no hacemos nada
			}

			BCryptPasswordEncoder enc = new BCryptPasswordEncoder();

			User u = new User();
			u.setEmail(superAdminEmail);
			u.setPassword(enc.encode("super123")); // contraseña del súper admin
			u.setRole("SUPER_ADMIN");
			u.setEnabled(true);

			u.setNombre("Super");
			u.setApellido("Admin");
			u.setRut("11.111.111-1");  // cualquiera válido
			u.setCalle("Sin dirección");
			u.setNumeroCasa("0");
			u.setNumeroDepto(null);
			u.setComuna("N/A");
			u.setTelefono("000000000");
			u.setFotoPerfilUrl(null);

			userRepository.save(u);
			System.out.println("SUPER_ADMIN creado: " + superAdminEmail + " / super123");
		};
	}
}
