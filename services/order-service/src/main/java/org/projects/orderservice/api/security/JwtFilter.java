package org.projects.orderservice.api.security;

import java.io.IOException;
import java.util.List;

import org.projects.orderservice.api.customer.Role;
import org.projects.orderservice.api.customer.User;
import org.projects.orderservice.api.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NotBlank
            HttpServletRequest request,
            @NotBlank
            HttpServletResponse response,
            @NotBlank
            FilterChain filterChain
    ) throws ServletException, IOException{

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        username= jwtService.extractUsername(jwt);

        if (username!= null && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<Role> roles = jwtService.getAuthorities(jwt);
            final UserDetails userDetails = User.builder()
                                            .email(username)
                                            .roles(roles)
                                            .enabled(true)
                                            .locked(false)
                                            .build();

            if (jwtService.isTokenValid(jwt, username)) {
                final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
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
