package com.dani.livechatservice.auth;

import com.dani.livechatservice.config.JwtAuthService;
import com.dani.livechatservice.exception.ErrorsEnum;
import com.dani.livechatservice.user.Role;
import com.dani.livechatservice.user.User;
import com.dani.livechatservice.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthService jwtAuthService;
    private final AuthenticationManager authenticationManager;

    @Value("${lcs.auth.jwt-expiration}")
    private Integer ACCESS_TOKEN_EXPIRATION;
    @Value("${lcs.auth.refresh-expiration}")
    private Integer REFRESH_TOKEN_EXPIRATION;

    public AuthenticationResponse register(RegistrationRequest request) throws Exception {
        var user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        log.info("Registration: saving user {}", user);

        try {
            var savedUser = userService.saveUser(user);
            log.info("Registration: new user saved {}", savedUser);
        }catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Username already exists.");
        }

        return this.generateAuthenticationResponse(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userService.getUserByUsername(request.username());

        if(user == null)
            throw new UsernameNotFoundException(ErrorsEnum.USER_NOT_FOUND.getErrorMessage());

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return this.generateAuthenticationResponse(user);
    }

    public AuthenticationResponse refreshToken(String refreshToken) {
        refreshToken = refreshToken.replace("Bearer ", "");
        var claims = jwtAuthService.extractAllClaims(refreshToken);
        var username = claims.getSubject();

        var user = userService.getUserByUsername(username);
        var newTokenExpiration = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION);
        var newToken = jwtAuthService.generateToken(user, newTokenExpiration);

        return AuthenticationResponse.builder()
                .refreshToken(refreshToken)
                .expiration(claims.getExpiration())
                .token(newToken)
                .expiration(newTokenExpiration)
                .build();
    }

    private AuthenticationResponse generateAuthenticationResponse(UserDetails user) {
        var tokenExpiration = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION);
        var refreshTokenExpiration = new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION);
        var authToken = jwtAuthService.generateToken(user, tokenExpiration);
        var refreshToken = jwtAuthService.generateToken(user, refreshTokenExpiration);

        return AuthenticationResponse.builder()
                .token(authToken)
                .expiration(tokenExpiration)
                .refreshToken(refreshToken)
                .refreshTokenExpiration(refreshTokenExpiration)
                .build();
    }
}
