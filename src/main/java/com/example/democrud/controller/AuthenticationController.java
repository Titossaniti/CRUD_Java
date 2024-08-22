package com.example.democrud.controller;

import com.example.democrud.model.LoginUserModel;
import com.example.democrud.model.RegisterUserModel;
import com.example.democrud.model.User;
import com.example.democrud.responses.LoginResponse;
import com.example.democrud.service.AuthenticationService;
import com.example.democrud.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserModel registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserModel loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String token = jwtService.generateToken(authenticatedUser);

        // Supposons que vous avez une méthode qui retourne le temps d'expiration en secondes
        long expiresIn = jwtService.getExpirationInSeconds(token);

        LoginResponse response = new LoginResponse(token, expiresIn);
        return ResponseEntity.ok(response);
    }
}