package com.example.secureapihateoas.service;

import com.example.secureapihateoas.model.Users;
import com.example.secureapihateoas.repository.UserRepository;
import com.example.secureapihateoas.dto.RegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
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



}
