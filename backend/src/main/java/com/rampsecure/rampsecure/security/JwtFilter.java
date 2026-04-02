package com.rampsecure.rampsecure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        //HANDLE PREFLIGHT FIRST
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        //GET AUTH HEADER
        String authHeader = request.getHeader("Authorization");
        if (authHeader ==null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }

        //EXTRACT THE TOKEN AUTH HEADER
        String extractedToken = authHeader.substring(7);

        //EXTRACT THE USERNAME FROM THE JWTUTIL CLASS

        String extractUsername = jwtUtil.extractUsername(extractedToken);

        //VALIDATE
        Boolean validate = jwtUtil.validateToken(extractedToken,extractUsername);

        if(validate){
            String role = jwtUtil.extractRole(extractedToken);
            String station = jwtUtil.extractStation(extractedToken);
            UUID id =jwtUtil.extractId(extractedToken);
            var authenticated = new UsernamePasswordAuthenticationToken(
                    extractUsername,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role))
            );
            // critical step that tells Spring Security "this user is authenticated"
            AuthDetails authDetails = new AuthDetails(id,station);
            authenticated.setDetails(authDetails);
            SecurityContextHolder.getContext().setAuthentication(authenticated);
        }

        filterChain.doFilter(request,response);
    }
}
