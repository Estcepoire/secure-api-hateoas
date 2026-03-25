package com.example.secureapihateoas.service;

import com.example.secureapihateoas.dto.LoginDTO;
import com.example.secureapihateoas.entities.Users;
import com.example.secureapihateoas.repository.UserRepository;
import com.example.secureapihateoas.dto.RegisterDTO;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public Users register(RegisterDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already exist!");
        }
        
        Users user = new Users();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("ROLE_USER");
        
        return userRepository.save(user);
    }

    public String login(@NonNull LoginDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        return jwtService.generateToken(userDetails.getUsername(), role);
    }



}
