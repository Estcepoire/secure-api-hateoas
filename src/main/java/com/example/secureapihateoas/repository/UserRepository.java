package com.example.secureapihateoas.repository;

import com.example.secureapihateoas.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmailAndPassword(String email, String password);

    Optional<Users> findByEmail(String email);
}
