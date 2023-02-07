package com.dani.livechatservice.auth;

import com.dani.livechatservice.config.JwtAuthService;
import com.dani.livechatservice.user.Role;
import com.dani.livechatservice.user.User;
import com.dani.livechatservice.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthService jwtAuthService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegistrationRequest request) {
        var user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        log.info("Registration: saving user {}", user);

        var savedUser = userService.saveUser(user);

        log.info("Registration: new user saved {}", savedUser);

        var authToken = jwtAuthService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(authToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var user = userService.getUserByUsername(request.username());

        var authToken = jwtAuthService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(authToken)
                .build();
    }
}
