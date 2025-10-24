package com.example.library_system.config;

import com.example.library_system.repository.UserRepository;
import com.example.library_system.entity.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email) //Hämta användare från databas
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Konvertera till Spring Security's UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())                    // Använd email som username
                .password(user.getPassWord())                 // Krypterat lösenord från DB
                .authorities("ROLE_" +user.getRole())        // Spring Security kräver "ROLE_" prefix
                .disabled(!user.isEnabled())                 //OBS! // Inverterar enabled för disabled
                .build();
    }
}

