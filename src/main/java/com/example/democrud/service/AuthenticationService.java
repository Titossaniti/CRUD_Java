package com.example.democrud.service;

import com.example.democrud.model.LoginUserModel;
import com.example.democrud.model.RegisterUserModel;
import com.example.democrud.model.User;
import com.example.democrud.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User signup(RegisterUserModel input) {
        User user = new User();
        user.setName(input.getName());
        user.setEmail(input.getEmail());
        user.setAge(input.getAge());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole("USER");  // Assigne le rôle USER par défaut lors de l'inscription

        return userRepository.save(user);
    }

    public User authenticate(LoginUserModel input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
        );

        return userRepository.findByEmail(input.getEmail()).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}
