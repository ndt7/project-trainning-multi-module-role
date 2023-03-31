package com.example.auth;

import com.example.dto.*;
import com.example.entity.People;
import com.example.entity.Token;
import com.example.repository.PeopleRepository;
import com.example.repository.TokenRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

/// dịch vụ xác thực jwt
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PeopleRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(@RequestBody @Valid RegisterRequest request) {

        var people = People.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail().toString())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.TEACHER)
                .build();
        Optional<People> userExit = repository.findByEmail(request.getEmail().toString());   /// tìm kiếm trong csdl
        if (userExit.isEmpty()) {
            var savedUser = repository.save(people);
            var jwtToken = jwtService.generateToken(people);
            saveUserToken(savedUser, jwtToken);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else {
            return null;
        }

    }

    public AuthenticationResponse registerAdmin(@RequestBody @Valid RegisterRequest request) {
        var people = People.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail().toString())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();
        Optional<People> userExit = repository.findByEmail(request.getEmail().toString());
        if (userExit.isPresent()) {
            return null;
        }
        var savedUser = repository.save(people);
        var jwtToken = jwtService.generateToken(people);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()));
            var user = repository.findByEmail(request.getEmail());   /// tim kiem trong csdl;
            if (user.isPresent()) {
                var jwtToken = jwtService.generateToken(user.get());   /// generate token từ user
                revokeAllUserTokens(user.get());
                saveUserToken(user.get(), jwtToken);
                return AuthenticationResponse.builder()
                        .token(jwtToken)
                        .build();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private void saveUserToken(People user, String jwtToken) {
        var token = Token.builder()
                .people(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(People user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());    // tìm kiến tokens theo ID user
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
