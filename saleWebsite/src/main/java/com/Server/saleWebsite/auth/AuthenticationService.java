package com.Server.saleWebsite.auth;


import com.Server.saleWebsite.config.JwtService;
import com.Server.saleWebsite.role.Role;
import com.Server.saleWebsite.token.TokenRepository;
import com.Server.saleWebsite.token.TokenType;
import com.Server.saleWebsite.token.token;
import com.Server.saleWebsite.user.User;
import com.Server.saleWebsite.user.UserRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request) {
        try {
            String jwtToken;
            String refreshToken;
                userRepository.findByEmail(request.getEmail())
                        .ifPresent(user -> {
                            throw new RuntimeException("Email already exists");
                        });
                User user = User.builder()
                        .full_name(request.getFull_name())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .sex(request.getSex())
                        .phone(request.getPhone())
                        .birth_day(request.getBirthDay())
                        .address(request.getAddress())
                        .role(Role.STUDENT)
                        .school_name(request.getSchool_name())
                        .create_date(new Timestamp(System.currentTimeMillis()))
                        .build();

                User savedUser = userRepository.save(user);



                jwtToken = jwtService.generateToken(savedUser);
                refreshToken = jwtService.generateRefreshToken(savedUser);
                savedUserToken(savedUser, jwtToken);
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to register user: " + e.getMessage());
        }
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        savedUserToken(savedUser,jwtToken);
        System.out.println("Email đã đăng nhập là: " + request.getEmail());
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();

    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUser_id());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void savedUserToken(User user, String jwtToken) {
        var Token = token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(Token);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                savedUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }


}
