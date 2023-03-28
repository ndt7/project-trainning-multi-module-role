package com.example.auth;

import com.example.dto.*;
import com.example.entity.People;
import com.example.entity.Token;
import com.example.repository.PeopleRepository;
import com.example.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    /// ĐĂNG KÝ NGƯỜI DÙNG MỚI
    public AuthenticationResponse register(RegisterRequest request) {
        var people = People.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .build();
        Optional<People> userExit = repository.findByEmail(request.getEmail());   /// tìm kiếm trong csdl
        if (userExit.isPresent()) {    /// nếu tồn tại rồi
            var jwtToken = jwtService.generateToken(people);   //// thì generate token theo user da tim thay
            saveUserToken(userExit.get(), jwtToken);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
        var savedUser = repository.save(people);   /// luu vao csdl
        var jwtToken = jwtService.generateToken(people);   //// generate token
        saveUserToken(savedUser, jwtToken);   /// luu token
        return AuthenticationResponse.builder()     ///tra ve cho nguoi dung
                .token(jwtToken)
                .build();
    }

    /// authentication tra ve token, xac thuc tai khoan
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()));
        var user = repository.findByEmail(request.getEmail())   /// tim kiem trong csdl
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);   /// generate token từ user
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /// luu token vao csdl
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

    /// thu hồi tất cả token người dùng trước đây
    private void revokeAllUserTokens(People user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());    // tìm kiến tokens theo ID user
        if (validUserTokens.isEmpty())   /// kiểm tra xem có null không
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);    /// gán cho token đã hết hạn
            token.setRevoked(true);    //// gán cho token đã thu hồi
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
