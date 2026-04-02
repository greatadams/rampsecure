package com.rampsecure.rampsecure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                //CSRF protection is for browser sessions, not needed for JWT APIs.
                .csrf(csrf -> csrf.disable())
                // your server stores no session state, every request must carry its own token.
                .sessionManagement(session ->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        //login and register endpoints are public, everything else requires authentication.
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/transaction/**").hasAnyAuthority("ROLE_SUPERVISOR", "ROLE_SAFETY_OFFICER","ROLE_ADMIN")
                        .requestMatchers("/api/equipment/all").hasAnyAuthority("ROLE_SUPERVISOR", "ROLE_SAFETY_OFFICER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/equipment/*/status").hasAnyAuthority("ROLE_SUPERVISOR", "ROLE_SAFETY_OFFICER", "ROLE_ADMIN")
                        .requestMatchers("/api/user/**").hasAnyAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                //tells Spring to run your JwtFilter before its own authentication filter.
                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173","https://rampsecure.vercel.app"));
//        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173", "https://rampsecure.vercel.app"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    //the password hashing algorithm you'll use when saving passwords.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
