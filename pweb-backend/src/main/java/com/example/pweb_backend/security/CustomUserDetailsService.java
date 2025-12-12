package com.example.pweb_backend.security;

import com.example.pweb_backend.model.User;
import com.example.pweb_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String role = "ROLE_" + u.getRole(); // SUPER_ADMIN -> ROLE_SUPER_ADMIN

        return new org.springframework.security.core.userdetails.User(
                u.getEmail(),
                u.getPassword(),
                u.getEnabled(),   // cuenta activa
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}

