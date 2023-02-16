package com.shehzab.recipewebsite.controller;

import com.shehzab.recipewebsite.model.LoginPojo;
import com.shehzab.recipewebsite.model.RegisterUser;
import com.shehzab.recipewebsite.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UsersRepository usersRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUser registerUser) {
        try {
            var userExist = usersRepository.findByEmail(registerUser.getEmail());
            if (userExist != null) {
                return ResponseEntity.status(400).body("Email already exist");
            }
            registerUser.setPassword(passwordEncoder.encode(registerUser.getPassword()));
            registerUser.setCreated(new Date());
            usersRepository.save(registerUser);
            return ResponseEntity.ok(registerUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/emailValidation")
    public ResponseEntity<?> checkEmailExist(@RequestParam(name = "email") String email) {
        if (email == null) {
            return ResponseEntity.status(400).body("param is missing");
        }
        var userEmail = usersRepository.findByEmail(email);
        if (userEmail == null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(409).body("email already exist");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginPojo loginPojo) {
        try {
            var user = usersRepository.findByEmail(loginPojo.email());
            if (user != null) {
                if (passwordEncoder.matches(loginPojo.password(), user.getPassword())) {
                    return ResponseEntity.ok("valid user");
                }
            }
            return ResponseEntity.status(401).body("invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
