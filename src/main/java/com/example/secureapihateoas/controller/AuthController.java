package com.example.secureapihateoas.controller;

import com.example.secureapihateoas.dto.AuthResponseDTO;
import com.example.secureapihateoas.dto.LoginDTO;
import com.example.secureapihateoas.dto.RegisterDTO;
import com.example.secureapihateoas.entities.Users;
import com.example.secureapihateoas.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterDTO registerDTO) {
        Users newUser = authService.register(registerDTO);
        
        AuthResponseDTO response = new AuthResponseDTO();
        response.setEmail(newUser.getEmail());
        response.setRole(newUser.getRole());
        response.setName(newUser.getName());
        response.setMessage("User successfully registered");
        
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuthController.class).register(registerDTO)).withSelfRel();
        response.add(selfLink);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        String token = authService.login(loginDTO);
        
        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setEmail(loginDTO.getEmail());
        response.setMessage("Login successful");
        
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuthController.class).login(loginDTO)).withSelfRel();
        response.add(selfLink);
        
        return ResponseEntity.ok(response);
    }

}
