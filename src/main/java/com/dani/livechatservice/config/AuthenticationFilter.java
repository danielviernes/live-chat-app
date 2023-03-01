package com.dani.livechatservice.config;

import com.dani.livechatservice.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

import static com.dani.livechatservice.config.Constants.BEARER_AUTH_PREFIX;
import static com.dani.livechatservice.config.Constants.HEADER_KEY_AUTHORIZATION;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtAuthService jwtAuthService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.info("--------- Authentication filter start ---------");

        final String authHeader = request.getHeader(HEADER_KEY_AUTHORIZATION);
        final String token;
        final String username;

        if(authHeader == null || !authHeader.startsWith(BEARER_AUTH_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(7);
        try {
            username = jwtAuthService.extractUsername(token);
        } catch(ExpiredJwtException e) {
            log.error("Token is expired");
            response.setStatus(401);
            response.setHeader("error", e.getMessage());
            response.setHeader("isTokenExpired", "true");
            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getOutputStream(), Map.of("error_message", "Token is expired"));
            return;
        }

        if(SecurityContextHolder.getContext().getAuthentication() == null
                && username != null) {
            UserDetails userDetails = this.userService.getUserByUsername(username);
            if(jwtAuthService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        log.info("--------- Authentication filter end ---------");

        filterChain.doFilter(request, response);
    }
}
