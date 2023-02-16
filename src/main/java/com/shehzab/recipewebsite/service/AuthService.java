package com.shehzab.recipewebsite.service;

import com.shehzab.recipewebsite.entity.User;
import com.shehzab.recipewebsite.model.AuthenticationRequest;
import com.shehzab.recipewebsite.model.AuthenticationResponse;
import com.shehzab.recipewebsite.model.RegisterRequest;
import com.shehzab.recipewebsite.repository.UsersRepository;
import com.shehzab.recipewebsite.securityconfig.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public Boolean isEmailExist(String email) {
        var user = usersRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        return true;
    }

    public AuthenticationResponse register(RegisterRequest registerRequest) throws HttpClientErrorException {
        if (isEmailExist(registerRequest.getEmail())) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "email already exist");
        }
        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .created(new Date())
                .build();
        usersRepository.save(user);
        var jwtToken = jwtTokenProvider.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()
        ));
        var user = usersRepository.findByEmail(authenticationRequest.getEmail());
        var jwtToken = jwtTokenProvider.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
