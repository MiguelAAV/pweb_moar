package com.example.pweb_backend.config;

import com.example.pweb_backend.model.Product;
import com.example.pweb_backend.model.User;
import com.example.pweb_backend.repository.ProductRepository;
import com.example.pweb_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepo, ProductRepository productRepo) {
        return args -> {

            if (userRepo.findByEmail("admin@tienda.com").isEmpty()) {
                User admin = new User();
                admin.setEmail("admin@tienda.com");
                admin.setPassword("admin123");
                admin.setRole("ADMIN");
                userRepo.save(admin);
            }

            if (productRepo.count() == 0) {

                Product p1 = new Product();
                p1.setNombre("iPhone 13");
                p1.setBrand("Apple");
                p1.setDescripcion("Smartphone Apple iPhone 13");
                p1.setPrecio(800000);
                p1.setStock(10);

                Product p2 = new Product();
                p2.setNombre("Samsung Galaxy S23");
                p2.setBrand("Samsung");
                p2.setDescripcion("Smartphone Samsung Galaxy S23");
                p2.setPrecio(700000);
                p2.setStock(5);

                productRepo.save(p1);
                productRepo.save(p2);
            }
        };
    }
}

