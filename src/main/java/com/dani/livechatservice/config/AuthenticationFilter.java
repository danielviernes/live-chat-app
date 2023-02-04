package com.dani.livechatservice.config;

import com.dani.livechatservice.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.dani.livechatservice.config.Constants.BEARER_AUTH_PREFIX;
import static com.dani.livechatservice.config.Constants.HEADER_KEY_AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AuthorizationService authorizationService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(HEADER_KEY_AUTHORIZATION);
        final String token;
        final String username;

        if(authHeader == null || !authHeader.startsWith(BEARER_AUTH_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(7);
        username = authorizationService.extractUsername(token);

        if(SecurityContextHolder.getContext().getAuthentication() == null
                && username != null) {
            UserDetails userDetails = this.userService.loadUserByUsername(username);
            if(authorizationService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
